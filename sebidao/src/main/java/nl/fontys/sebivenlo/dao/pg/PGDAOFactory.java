package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
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

/**
 * DAO Factory for postgresql database.
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

    final Map<Class<?>, Function<?, ?>> marshallInMap = new ConcurrentHashMap<>();

    {
        registerInMarshaller(  LocalDate.class, ( Date d ) -> d.toLocalDate() );
    }

    /**
     * Get a connection to be used by the DAOs created by this factory. 
     * 
     * @return a connection
     * @throws SQLException when connection cannot be retrieved.
     */
    final Connection getConnection() throws SQLException {
        Connection con = ds.getConnection();
//        if ( con instanceof PGConnection && !pgTypeMap.isEmpty() ) {
//            final PGConnection pgcon = (PGConnection) con;
//            for ( Map.Entry<String, Class<? extends PGobject>> entry : pgTypeMap
//                    .entrySet() ) {
//                pgcon.addDataType( entry.getKey(), entry.getValue() );
//            }
//        }
        if ( sqlTypeMap != null ) {
            Map<String, Class<?>> typeMap = con.getTypeMap();
            if ( typeMap == null ) {
                typeMap = new HashMap<>();
            }
            typeMap.putAll( sqlTypeMap );
        }
        return con;
    }

//    /**
//     * Register a pg data type or user type.
//     *
//     * @param name postgresql type name
//     * @param type java type, e.g. TsRange like in this package.
//     */
//    public final void registerPGdataType( String name, Class<? extends PGobject> type ) {
//        pgTypeMap.put( name, type );
//    }

    private Map<String, Class<?>> sqlTypeMap;

    <U> U marshallIn( Class<U> type, Object in ) {
        return ( (Function<Object, U>) marshallInMap
                .getOrDefault( type, ( x ) -> x ) ).apply( in );
    }
    
    /**
     * Add an in marshaller to this factory, so the connection can apply it on data  T coming from 
     * the PostgreSQL database and map it to the required user type U.
     * 
     * PostgreSQL has a rich type set beyond the standard set defined for java.sql. Those types are typically a
     * subclass of PGobject, which is relatively easily mapped to a user type. An example would be
     * TSDange, which is best expressed as a time range with begin and end point of LocalDateTime.
     * 
     * When the user defines a LocalDateTimeRange matching the semantics of TSRange, and provides a mapper 
     * from tsrange to LocalDataTimeRange, than such mapper can be registerd to this factory, so that the Dao can easily 
     * map from the PGobject to the user object.
     * 
     * <pre class='brush:java'>
     *   
     *     pgdaofactory.registerInMarshaller( LocalDateTimeRange.class, ( PGobject pgo ) -{@literal >} LocalDateTimeRange.of(pgo));
     *       
     * </pre>
     * 
     * 
     * @param <T> incoming type
     * @param <U> user type 
     * @param targetType type token for output
     * @param mapper the mapper function, typically a lambda.
     */
    public final <T extends Serializable,U> void registerInMarshaller(Class<U> targetType, Function<T,U> mapper){
        this.marshallInMap.put( targetType, mapper );
    }

}
