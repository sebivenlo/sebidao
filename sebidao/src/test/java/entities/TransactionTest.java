package entities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import nl.fontys.sebivenlo.dao.pg.PGTransactionToken;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TransactionTest {

    static PGDataSource ds = PGDataSource.DATA_SOURCE;
    static PGDAOFactory daof;

    @BeforeClass
    public static void setupClass() throws IOException, SQLException {
        DBTestHelpers.loadDatabase();
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper() );
        daof.registerMapper( Department.class, new DepartmentMapper() );
    }

    DAO<Integer, Employee> checkEmpDao;
    DAO<Integer, Department> checkDepDao;

    @Before
    public void setup() {
        checkEmpDao = daof.createDao( Employee.class );
        checkDepDao = daof.createDao( Department.class );
    }

    @Test
    public void testRollback() {
        // first dao is consumed by transaction
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee henk = new Employee( 0, "Heijmans", "Henk",
                "henk@someclub.org", 1 );

        int beforeSize = edao.size();
        try {
            TransactionToken tok = edao.startTransaction();
            edao.save( henk );
            tok.rollback();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
        int afterSize = checkEmpDao.size();

        assertEquals( "size changed, although transaction rolled back ",
                beforeSize, afterSize );
        //fail( "testAddDeptWithBoss not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testAddDeptWithBossRollBack() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        DAO<Integer, Department> ddao = daof.createDao( Department.class );
        Department engineering = new Department( 0, "Engineering",
                "Where value creation happens", "dilbert@example.com" );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();

        try ( TransactionToken tok = ddao.startTransaction(); ) {
            edao.setTransactionToken( tok );
            Department save = ddao.save( engineering );
            int depno = save.getDepartmentid();
            dilbert.setDepartmentid( depno );
            edao.save( dilbert );
            int tempSize = edao.size();
            assertEquals( "temp emp size", empSize + 1, tempSize );
            tok.rollback();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        int newDepSize = checkDepDao.size();
        int newEmpSize = checkEmpDao.size();

        assertTrue( "both table should stay same sized", newDepSize == deptSize
                && newEmpSize == empSize );
//        fail( "testMethod not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testAddDeptWithBossCommit() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        DAO<Integer, Department> ddao = daof.createDao( Department.class );
        Department engineering = new Department( 0, "Engineering",
                "Where value creation happens", "dilbert@example.com" );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();
        Department savedDept = null;
        Employee savedDilbert = null;

        try {
            TransactionToken tok = ddao.startTransaction();
            System.out.println( "tok = " + tok );
            edao.setTransactionToken( tok );
            savedDept = ddao.save( engineering );
            int depno = savedDept.getDepartmentid();
            dilbert.setDepartmentid( depno );
            savedDilbert = edao.save( dilbert );
            System.out.println( "savedDilbert = " + savedDilbert );
            tok.commit();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        int newDepSize = checkDepDao.size();
        System.out.println( "newDepSize = " + newDepSize );
        int newEmpSize = checkEmpDao.size();
        System.out.println( "newEmpSize = " + newEmpSize );

        // cleanup
        checkEmpDao.delete( savedDilbert );
        checkDepDao.delete( savedDept );
        assertTrue( "both table should have grown", newDepSize == deptSize + 1
                && newEmpSize == empSize + 1 );
//        fail( "testAddDeptWithBossCommit not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetAll() {

        DAO<Integer, Employee> edao = daof.createDao( Employee.class );

        try {
            TransactionToken tok = edao.startTransaction();
            Collection<Employee> all = edao.getAll();
            Connection connection = ( (PGTransactionToken) tok ).getConnection();
            assertFalse( "connection should stay open", connection.isClosed() );
            System.out.println( "all = " + all );
            int size = edao.size();
            System.out.println( "size = " + size );
            assertEquals( "two calls same size", size, all.size() );
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        //fail( "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetPiet() throws SQLException {

        DAO<Integer, Employee> edao = daof.createDao( Employee.class );

        try (
                TransactionToken tok = edao.startTransaction(); ) {
            Optional<Employee> optionalPiet = edao.get( 1 );

            assertTrue( "do we have anything Piet", optionalPiet.isPresent() );
            Connection connection = ( (PGTransactionToken) tok ).getConnection();
            assertFalse( "connection should stay open", connection.isClosed() );
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        assertConnectionClosed( edao );
    }

    private void assertConnectionClosed( DAO<Integer, Employee> edao ) throws
            SQLException {
        TransactionToken transactionToken = edao.getTransactionToken();
        assertNotNull( "token still there?", transactionToken );
        Connection connection
                = ( (PGTransactionToken) transactionToken ).getConnection();
        assertTrue( "after use, connection closed", connection.isClosed() );
        // fail( "testGetPiet not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testDelete() throws SQLException {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        try ( TransactionToken tok = edao.startTransaction(); ) {
            Employee johnny = new Employee( 0, "Cash", "Johnny",
                    "sue@nashville.town", 1 );
            Employee savedJohnny = edao.save( johnny );
            System.out.println( "Short appearance of Johnny: " + savedJohnny );
            edao.delete( savedJohnny );
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        assertConnectionClosed( edao );

        //fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testUpdate() throws SQLException {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        try ( TransactionToken tok = edao.startTransaction(); ) {
            Employee johnny = new Employee( 0, "Cash", "Johnny",
                    "sue@nashville.town", 1 );
            Employee sue = edao.save( johnny );
            sue.setFirstname( "Sue" );

            System.out.println( "Short appearance of Johnny: " + sue );
            Employee newJohn = edao.update( sue );
            assertFalse( "connection still open?", ( (PGTransactionToken) tok ).
                    getConnection().isClosed() );
            System.out.println( "newJohn = " + newJohn );
            assertEquals( "new name", "Sue", newJohn.getFirstname() );
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
        assertConnectionClosed( edao );
        //fail( "testUpdate not yet implemented. Review the code and comment or delete this line" );
    }
}
