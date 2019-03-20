package nl.fontys.sebivenlo.dao;

import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Map an entity to and from its representation in the persistent storage.
 *
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <K> key for access.
 * @param <E> mapped type
 */
public interface Mapper<K, E> {

    /**
     * Get the type that is mapped by this mapper.
     * @return the type
     */
    Class<E> entityType();

    /**
     * Read the persistent fields of the object as an array.
     *
     * @param e the object to read.
     * @return the fields as array.
     */
    Object[] explode( E e );

    /**
     * Create a new object from the given parts.
     *
     * @param parts to construct the object from;
     * @return the new E
     */
    E implode( Object[] parts );

    /**
     * Get the table name for the entity.
     *
     * @return the name of the table in the database
     */
    default String tableName() {
        return entityType().getSimpleName().toLowerCase() + 's';
    }

    /**
     * Get the column name(s) for the key column(e), typically the forming
     * primary key.
     *
     * Note that the minimal, but also default length of the returned array is
     * one.
     *
     * @return the id column name
     */
    default Set<String> keyNames() {
        return new LinkedHashSet<>( asList( idName() ) );
    }

    /**
     * Get the id from the entity.
     *
     * @return a method to retrieve the id.
     */
    Function<E, K> keyExtractor();

    /**
     * Get the column names in the database in definition order. Note that this
     * method can be implemented by using the information schema.
     *
     * @return the set of field names.
     */
    Set<String> persistentFieldNames();

    /**
     * Does the persistence layer generate the key, or will it be provided by
     * the application.
     *
     * In case the business has a natural key, overwrite this method set to
     * return false.
     *
     * In a use case where keys are generated, as in a database, you would use e
     * sequence number.
     *
     * return true when the key is generated.
     *
     * @return true by default, but may very per implementation
     */
    default boolean generateKey() {
        return true;
    }

    /**
     * Get the name of the id field or column.
     * @return the name.
     */
    default String idName() {
        return entityType().getSimpleName().toLowerCase() + "id";
    }
    
    /**
     * Get the name of the natural key of this relation, if it is not the default.
     * @return the key name
     */
    default String naturalKeyName(){
        return idName();
    }
}
