package nl.fontys.sebivenlo.dao.pg;

import java.sql.Connection;
import java.sql.SQLException;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 * 
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGTransactionToken implements TransactionToken {

    private final Connection connection;

    public PGTransactionToken( Connection connection ) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit( false );
    }

    @Override
    public void rollback() throws Exception {
        this.connection.rollback();
    }

    @Override
    public void commit() throws Exception {
        this.connection.commit();
    }

    public Connection getConnection() {
        return connection;
    }
    
    
}
