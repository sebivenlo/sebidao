package nl.fontys.sebivenlo.dao.pg;

import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.SimpleEntity;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOFactory extends AbstractDAOFactory {

    private final DataSource ds;

    /**
     * Inject the datasource using this ctor.
     * @param ds the data source
     */
    public PGDAOFactory( DataSource ds ) {
        this.ds = ds;
    }
    
    @Override
    public <K, E> DAO createDao(
            Class<? extends SimpleEntity> forClass ) {
        return new PGDAO( ds, this.mappers.get( forClass ) );
    }

    @Override
    public <K, E> DAO createDao(
            Class<? extends SimpleEntity> forClass, TransactionToken token ) {
        return new PGDAO( ds, this.mappers.get( forClass ) ).
                setTransactionToken( token );
    }

}
