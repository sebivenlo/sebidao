package nl.fontys.sebivenlo.dao.memory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.SimpleEntity;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class MemoryDAOFactory extends AbstractDAOFactory {

    private static class Holder {

        static final MemoryDAOFactory INSTANCE = new MemoryDAOFactory();
    }

    /**
     * Get the instance of this singleton MemoryDAOFactory.
     *
     * @return the instance.
     */
    public static MemoryDAOFactory getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<Class<?>, InMemoryDAO<? extends Serializable, ? extends Entity2>> factorStorage
            = new HashMap<>();

    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao(
            Class<E> forClass ) {
        return factorStorage.computeIfAbsent( forClass, c
                -> new InMemoryDAO( c ) );
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao(
            Class<E> forClass,
            TransactionToken token ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
