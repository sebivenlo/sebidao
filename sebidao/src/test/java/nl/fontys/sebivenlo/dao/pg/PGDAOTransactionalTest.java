package nl.fontys.sebivenlo.dao.pg;

import entities.Employee;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 * Make sure all tests run within a transaction.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
//@Ignore
public class PGDAOTransactionalTest extends PGDAOTest {

    PGTransactionToken tok;

    @Before
    @Override
    public void setUp() throws Throwable {
        super.setUp();
        eDao.close(); // close existing from super
        eDao = (PGDAO<Integer, Employee>) daof.createDao( Employee.class );
        try {
            tok = (PGTransactionToken) eDao.startTransaction();
        } catch ( SQLException notexpected ) {
            Logger.getLogger( PGDAOTransactionalTest.class.getName() ).
                    log( Level.SEVERE, null, notexpected );
        }
    }

    @After
    public void tearDown() throws Exception {
        
        tok.close();
    }
}
