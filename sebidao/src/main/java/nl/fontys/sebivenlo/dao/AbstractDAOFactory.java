package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Function;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public abstract class AbstractDAOFactory {

    protected final HashMap<Class<? extends SimpleEntity>, Mapper<? extends Serializable, ? extends SimpleEntity>> MAPPERS
            = new HashMap<>();

    public void registerMapper( Class<? extends SimpleEntity> forClass,
            Mapper<? extends Serializable, ? extends SimpleEntity> mappedBy ) {
        MAPPERS.put( forClass, mappedBy );
    }

    public abstract <K, E> DAO createDao( Class<? extends SimpleEntity> forClass );

    public abstract <K, E> DAO createDao( Class<? extends SimpleEntity> forClass,
            TransactionToken token );
}
