package nl.fontys.sebivenlo.dao.pg;

import java.sql.Connection;
import java.sql.SQLException;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 * Token carrying transaction information, such as the active connection.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGTransactionToken implements TransactionToken {

    private final Connection connection;

    /**
     * Create a token using a connection.
     * @param connection to hold
     * @throws SQLException when the connection can't be used.
     */
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
