package nl.fontys.sebivenlo.dao;

import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Map an entity to and from its representation in the persistent storage.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <K> key for access.
 * @param <E> mapped type
 * @deprecated do not implement this interface, instead extend AbstractMapper and do less work.
 */
@Deprecated
interface Mapper<K, E> {

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
    E implode( Object... parts );


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

}
