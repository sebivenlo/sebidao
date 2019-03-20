package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public abstract class AbstractDAOFactory {

    /**
     * Map  type to TypeMappper.
     */
    protected final HashMap<Class<? extends SimpleEntity>, 
            Mapper<? extends Serializable, ? extends SimpleEntity>> mappers
            = new HashMap<>();

    /**
     * Register  a type with its mapper.
     * @param forClass the class to be mapped
     * @param mappedBy this mapper
     */
    public void registerMapper( Class<? extends SimpleEntity> forClass,
            Mapper<? extends Serializable, ? extends SimpleEntity> mappedBy ) {
        mappers.put( forClass, mappedBy );
    }

    /**
     * Create a DAO for a given entity class indexed by a key class.
     *
     * @param <K> the key generic type
     * @param <E> the entity generic type
     * @param forClass actual type of the entity
     * @return the prepared DAO
     */
    public abstract <K, E> DAO createDao( Class<? extends SimpleEntity> forClass );

    /**
     *
     * Create a DAO for a given entity class indexed by a key class, prepared to
     * participate in a Transaction.
     *
     * @param <K> the key generic type
     * @param <E> the entity generic type
     * @param forClass actual type of the entity
     * @param token transaction token.
     * @return the prepared DAO
     */
    public abstract <K, E> DAO createDao( Class<? extends SimpleEntity> forClass,
            TransactionToken token );
}
