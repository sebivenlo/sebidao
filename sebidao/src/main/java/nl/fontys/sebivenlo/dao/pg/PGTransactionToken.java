package nl.fontys.sebivenlo.dao.pg;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 * Token carrying transaction information, such as the active connection.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGTransactionToken implements TransactionToken {

    private final Connection connection;

    /**
     * Create a token using a connection.
     *
     * @param connection to hold
     * @throws DAOException when the connection can't be used.
     */
    public PGTransactionToken( Connection connection ) {
        this.connection = connection;
        try {
            this.connection.setAutoCommit( false );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGTransactionToken.class.getName() ).
                    log( Level.SEVERE, null, ex );
            throw new DAOException( ex.getMessage(), ex );

        }
    }

    @Override
    public void rollback() throws Exception {
        System.out.println( "rollback" );
        this.connection.rollback();
    }

    @Override
    public void commit() throws Exception {
        System.out.println( "commit" );
        this.connection.commit();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }

    
}
