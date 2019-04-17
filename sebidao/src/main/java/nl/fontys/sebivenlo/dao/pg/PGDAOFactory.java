package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOFactory extends AbstractDAOFactory {

    private final DataSource ds;

    /**
     * Inject the datasource using this ctor.
     *
     * @param ds the data source
     */
    public PGDAOFactory( DataSource ds ) {
        this.ds = ds;
        queryStringCache = new HashMap<>();
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> PGDAO<K, E> createDao(
            Class<E> forClass ) {
        Map<String, String> queryTextCache = queryStringCache.computeIfAbsent( forClass, ( x ) -> new HashMap<>() );
        System.out.println( "queryTextCache = " + queryTextCache );
        System.out.println( "querytet size "+queryTextCache.size() );
        return new PGDAO( ds, this.mappers.get( forClass ), queryTextCache );
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> PGDAO<K, E> createDao(
            Class<E> forClass, TransactionToken token ) {
        Map<String, String> queryTextCache = queryStringCache.computeIfAbsent( forClass, ( x ) -> new HashMap<>() );
        System.out.println( "queryTextCache = " + queryTextCache );
        return new PGDAO( ds, this.mappers.get( forClass ), queryTextCache ).
                setTransactionToken( token );
    }

    final Map<Class<?>, Map<String, String>> queryStringCache;
}
