package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.pgranges.TSRange;
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
    final Map<Class<?>, Function<?, ?>> marshallInMap = new ConcurrentHashMap<>();
    final Map<Class<?>, Function<?, ?>> marshallOutMap = new ConcurrentHashMap<>();

    {
        marshallInMap.put( LocalDate.class, ( Date d ) -> d.toLocalDate() );
        marshallInMap.put( TSRange.class, ( PGobject d )
                -> new TSRange( d ) );
        marshallOutMap.put( LocalDate.class, ( LocalDate l ) -> java.sql.Date
                .valueOf( l ) );
    }

    /**
     * Get a connection to be used by the DAOs created by the factory. Loads any
     * mapped types using null null null
     * {@link PGDAOFactory#registerPGdataType(java.lang.String, java.lang.Class)}.
     *
     * @return a connection
     * @throws SQLException when connection cannot be retrieved.
     */
    final Connection getConnection() throws SQLException {
        Connection con = ds.getConnection();
        if ( con instanceof PGConnection && !pgTypeMap.isEmpty() ) {
            final PGConnection pgcon = (PGConnection) con;
            for ( Map.Entry<String, Class<? extends PGobject>> entry : pgTypeMap
                    .entrySet() ) {
                pgcon.addDataType( entry.getKey(), entry.getValue() );
            }
        }
        if ( sqlTypeMap != null ) {
            Map<String, Class<?>> typeMap = con.getTypeMap();
            if ( typeMap == null ) {
                typeMap = new HashMap<>();
            }
            typeMap.putAll( sqlTypeMap );
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

    private Map<String, Class<?>> sqlTypeMap;

    <U> U marshallIn( Class<U> type, Object in ) {
        return ( (Function<Object, U>) marshallInMap
                .getOrDefault( type, ( x ) -> x ) ).apply( in );
    }
}
