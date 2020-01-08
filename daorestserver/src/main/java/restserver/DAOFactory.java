package restserver;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.AbstractMapper;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.SimpleEntity;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.postgresql.util.PGobject;

/**
 * DAOFactory that support DataSource injection but otherwise delegates the work to
 * PGDAOFactory.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@Startup
@Singleton
public class DAOFactory extends AbstractDAOFactory {

    private PGDAOFactory realFactory;

    @Resource( lookup = "jdbc/fantys" )
    DataSource ds;


    @PostConstruct
    public void init() {
        realFactory = new PGDAOFactory( ds );
    }
    
    
    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao( Class<E> forClass ) {
        return realFactory.createDao( forClass );
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao( Class<E> forClass, TransactionToken token ) {
        return realFactory.createDao( forClass, token );
    }

    @Override
    public void registerMapper( Class<? extends SimpleEntity> forClass, AbstractMapper<? extends Serializable, ? extends SimpleEntity> mappedBy ) {
        realFactory.registerMapper( forClass, mappedBy );
    }

        /**
     * Register a pg data type or user type.
     *
     * @param name postgresql type name
     * @param type java type, e.g. TsRange like in this package.
     */
    public final void registerPGdataType( String name, Class<? extends PGobject> type ) {
        realFactory.registerPGdataType( name, type );
    }

    
}
