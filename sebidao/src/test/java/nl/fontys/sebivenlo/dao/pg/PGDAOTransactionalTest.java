package nl.fontys.sebivenlo.dao.pg;

import entities.Employee;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 * Make sure all tests run within a transaction.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
//@Ignore
public class PGDAOTransactionalTest extends PGDAOTestBase {

    PGTransactionToken tok;

    @Before
    @Override
    public void setUp() throws Throwable {
        super.setUp();
        eDao.close(); // close existing from super
        eDao = (PGDAO<Integer, Employee>) daof.createDao( Employee.class );
        try {
            tok = eDao.startTransaction();
        } catch ( DAOException notexpected ) {
            Logger.getLogger( PGDAOTransactionalTest.class.getName() ).
                    log( Level.SEVERE, null, notexpected );
        }
    }

    @After
    public void tearDown() throws Exception {
        
        tok.close();
    }
}
