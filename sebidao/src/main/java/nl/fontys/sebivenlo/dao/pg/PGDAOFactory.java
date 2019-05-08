package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.TransactionToken;
import org.postgresql.PGConnection;
import org.postgresql.util.PGobject;

/**
 * Factory for postgresql database.
 *
 * This factory registers TsRange, because it is required in the first project
 * that uses it and serves as a demo.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public final class PGDAOFactory extends AbstractDAOFactory {

    private final DataSource ds;

    /**
     * Inject the datasource using this ctor.
     *
     * @param ds the data source
     */
    public PGDAOFactory( DataSource ds ) {
        this.ds = ds;
        queryStringCache = new ConcurrentHashMap<>();
        this.pgTypeMap.put( "tsrange", TsRange.class );
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> PGDAO<K, E> createDao(
            Class<E> forClass ) {
        Map<String, String> queryTextCache = queryStringCache
                .computeIfAbsent( forClass, ( x ) -> new ConcurrentHashMap<>() );
        return new PGDAO( this, ds, this.mappers.get( forClass ), queryTextCache );
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> PGDAO<K, E> createDao(
            Class<E> forClass, TransactionToken token ) {
        return createDao( forClass ).setTransactionToken( token );
    }

    final Map<Class<?>, Map<String, String>> queryStringCache;

    final Map<String, Class<? extends PGobject>> pgTypeMap = new ConcurrentHashMap<>();

    /**
     * Get a connection to be used by the DAOs created by the factory. 
     * Loads any maped types using
     * {@link PGDAOFactory#registerPGdataType(java.lang.String, java.lang.Class).
     * 
     * @return a connection
     * @throws SQLException when connection cannot be retreived.
     */
    final Connection getConnection() throws SQLException {
        Connection con = ds.getConnection();
        if ( con instanceof PGConnection && !pgTypeMap.isEmpty() ) {
            final PGConnection pgcon = (PGConnection) con;
            for ( Map.Entry<String, Class<? extends PGobject>> entry : pgTypeMap.entrySet() ) {
                pgcon.addDataType( entry.getKey(), entry.getValue() );
            }
        }
        return con;
    }

    /**
     * Register a pg data type or user type.
     *
     * @param name postgresql type name
     * @param type java type, e.g. TsRange like in this package.
     */
    public final void registerPGdataType( String name, Class<? extends PGobject> type ) {
        pgTypeMap.put( name, type );
    }
}
