package nl.fontys.sebivenlo.dao;

import java.io.Serializable;

/**
 * The traditional DOA will just use a integral number as key or handle to the
 * entities. We choose {@code int} here, but {@code long} will do as well.
 *
 * To help the mapper in this package, an optional method
 * {@code Object[] asParts()} can be provided to provide both efficient and
 * simple dis-assembly of the such entity into its constituent parts, which will
 * help the database marshalling in the mapper.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public interface SimpleEntity extends Serializable {

    /**
     * Get the id of this entity.
     *
     * @return the id.
     */
    int getId();
}
