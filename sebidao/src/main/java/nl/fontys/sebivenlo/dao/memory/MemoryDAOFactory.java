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
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
public class MemoryDAOFactory extends AbstractDAOFactory {

    private static class Holder {

        public static MemoryDAOFactory instance = new MemoryDAOFactory();
    }

    /**
     * Get the instance of this singleton MemoryDAOFactory.
     * @return the instance.
     */
    public static MemoryDAOFactory getInstance() {
        return Holder.instance;
    }
    
    private Map<Class<?>, InMemoryDAO<? extends Serializable, ? extends Entity2>> factorStorage = new HashMap<>();

    @Override
    public <K, E> DAO createDao( Class<? extends SimpleEntity> forClass ) {
        return factorStorage.computeIfAbsent( forClass, c -> new InMemoryDAO( c ) );
    }

    @Override
    public <K, E> DAO createDao( Class<? extends SimpleEntity> forClass, TransactionToken token ) {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

}
