package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import static java.util.logging.Logger.*;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test if all sql exceptions are properly translated (as in wrapped in) into
 * DAOExceptions. For coverage. The methods all will find a connection that is
 * broken and throws an exception.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
//@Ignore
public class PGDAOConnectionClosedExceptionTest extends PGDAOExceptionTestBase {

    @Override
    Connection getConnection() {
        try {
            Connection realConnection = DBTestHelpers.ds.getConnection();
            return new NonClosingConnection( realConnection );
        } catch ( SQLException ex ) {
            getLogger( PGDAOConnectionClosedExceptionTest.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
        return null;
    }

    @Ignore
    @Test( expected = DAOException.class )
    public void testDelete() {
        eDao.delete( gp );
    }
}
