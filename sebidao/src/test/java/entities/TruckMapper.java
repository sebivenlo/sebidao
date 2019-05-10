package entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 * Map trucks.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckMapper extends AbstractMapper<Integer,Truck> {

    public TruckMapper( Class<Integer> keyType, Class<Truck> entityType ) {
        super( keyType, entityType );
    }

    
    @Override
    public Object[] explode( Truck t ) {
        return t.asParts();
    }

    @Override
    public Function<Truck, Integer> keyExtractor() {
        return Truck::getNaturalId;
    }

//    /**
//     * Get the table name for the entity.
//     *
//     * @return the name of the table in the database
//     */
//    default String tableName() {
//        return entityType().getSimpleName().toLowerCase() + 's';
//    }
//
//    /**
//     * Get the column name(s) for the key column(e), typically the forming
//     * primary key.
//     *
//     * Note that the minimal, but also default length of the returned array is
//     * one.
//     *
//     * @return the id column name
//     */
//    default Set keyNames() {
//        return new LinkedHashSet<>( asList( idName() ) );
//    }
//
//    /**
//     * Does the persistence layer generate the key, or will it be provided by
//     * the application.
//     *
//     * In case the business has a natural key, overwrite this method set to
//     * return false.
//     *
//     * In a use case where keys are generated, as in a database, you would use e
//     * sequence number.
//     *
//     * return true when the key is generated.
//     *
//     * Do not use this method. Use the Annotations ID and Generated instead to make more
//     * specific declarations on what is generated and what is not.
//     *
//     * @return true by default, but may very per implementation
//     * @deprecated this method should not be used.
//     */
//    @Deprecated
//    default boolean generateKey() {
//        return true;
//    }
//
//    /**
//     * Get the name of the id field or column.
//     * @return the name.
//     */
//    default String idName() {
//        return entityType().getSimpleName().toLowerCase() + "id";
//    }
//
//    /**
//     * Get the name of the natural key of this relation, if it is not the default.
//     * @return the key name
//     */
//    default String naturalKeyName() {
//        return idName();
//    }
    
}
