package nl.fontys.sebivenlo.dao.memory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.Entity2;
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

    private final Map<Class<?>, InMemoryDAO<? extends Serializable, ? extends Entity2>> factoryStorage
            = new HashMap<>();

    @Override
    public <K extends Serializable, E extends Entity2<K>> InMemoryDAO<K, E> createDao(
            Class<E> forClass ) {
        return factoryStorage.computeIfAbsent( forClass, c
                -> new InMemoryDAO( c ) );
    }

    /**
     * This implementation ignores the transaction token.
     * @param <K> key
     * @param <E> entity
     * @param forClass the enity type
     * @param token ignored
     * @return the DAO.
     */
    @Override
    public <K extends Serializable, E extends Entity2<K>> InMemoryDAO<K, E> createDao(
            Class<E> forClass,
            TransactionToken token ) {
        return createDao(forClass);
        //throw new UnsupportedOperationException( "Not supported yet." );
    }

}
