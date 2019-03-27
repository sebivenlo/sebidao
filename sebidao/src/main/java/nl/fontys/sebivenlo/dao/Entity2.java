package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.function.ToIntFunction;

/**
 * An entity that is accessible via a key.
 *
 * To be able to retrieve an entity from a backing persistence layer, it needs
 * to have a key. And yes, if you associate that with concept of a primary key,
 * you would be right.
 *
 * For those cases where SimpleEntity will not do, because you have to insist
 * on a natural key which is not an integral number such as {@code int} or {@code long}.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <K> key to the entity.
 */
public interface Entity2<K extends Serializable> extends SimpleEntity {

    /**
     * Get the key of this entity.
     *
     * @return the key.
     */
    K getNaturalId();

    /**
     * The backing store is easiest to implement using a simple number.
     *
     * @return the id in int form
     */
    @Override
    default int getId() {
        return idMapper().applyAsInt( getNaturalId() );
    }

    /**
     * Get the function to transform the natural and potential composite key to
     * a surrogate key.
     *
     * @return the function
     */
    ToIntFunction<K> idMapper();

}
