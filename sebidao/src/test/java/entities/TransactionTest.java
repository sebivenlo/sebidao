package entities;

import com.sun.javafx.geom.Edge;
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
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TransactionTest {

    static PGDataSource ds = PGDataSource.DATA_SOURCE;
    static PGDAOFactory daof;

    @BeforeClass
    public static void setupClass() throws IOException, SQLException {
        DBTestHelpers.loadDatabase();
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper2(Integer.class, Employee.class));
        daof.registerMapper( Department.class, new DepartmentMapper() );
    }

    DAO<Integer, Employee> checkEmpDao;
    DAO<String, Department> checkDepDao;

    @Before
    public void setup() {
        DBTestHelpers.loadDatabase();
        checkEmpDao = daof.createDao( Employee.class );
        checkDepDao = daof.createDao( Department.class );
    }

    @Test
    public void testRollback() {
        // first dao is consumed by transaction
        Employee henk = new Employee( 0, "Heijmans", "Henk",
                "henk@someclub.org", 1 );

        int beforeSize = checkEmpDao.size();
        try (
                DAO<Integer, Employee> edao = daof.createDao( Employee.class );
                TransactionToken tok = edao.startTransaction(); ) {
            Employee savedHenk = edao.save( henk );
            assertTrue("real employee id", savedHenk.getEmployeeid()!=0);
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
        Department engineering = new Department( 0, "Engineering",
                "Where value creation happens", "dilbert@example.com" );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();

        try (
                DAO<String, Department> ddao = daof.createDao( Department.class );
                TransactionToken tok = ddao.startTransaction();
                DAO<Integer, Employee> edao = daof.createDao( Employee.class, tok ); ) {
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
        Department engineering = new Department( 0, "Engineering",
                "Where value creation happens", "dilbert@example.com" );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();
        Department savedDept = null;
        Employee savedDilbert = null;

        try (
                DAO<String, Department> ddao = daof.createDao( Department.class );
                TransactionToken tok = ddao.startTransaction();
                DAO<Integer, Employee> edao = daof.createDao( Employee.class, tok ); ) {
            System.out.println( "tok = " + tok );
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

        try (
                DAO<Integer, Employee> edao = daof.createDao( Employee.class ); ) {
            TransactionToken tok = edao.startTransaction();
            Collection<Employee> all = edao.getAll();
            Connection connection = ( (PGTransactionToken) tok ).getConnection();
            assertFalse( "connection should stay open", connection.isClosed() );
            System.out.println( "all = " + all );
            int size = edao.size();
            System.out.println( "size = " + size );
            assertEquals( "two calls same size", size, all.size() );
            edao.close();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        //fail( "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetPiet() throws SQLException {

        try ( DAO<Integer, Employee> edao = daof.createDao( Employee.class );
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

    }

    @Test
    public void testDelete() throws SQLException {

        try ( DAO<Integer, Employee> edao = daof.createDao( Employee.class );
                TransactionToken tok = edao.startTransaction(); ) {
            Employee johnny = new Employee( 0, "Cash", "Johnny",
                    "sue@nashville.town", 1 );
            Employee savedJohnny = edao.save( johnny );
            System.out.println( "Short appearance of Johnny: " + savedJohnny );
            edao.delete( savedJohnny );
            tok.commit();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        //fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testUpdate() throws SQLException {
        //DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee johnny = new Employee( 0, "Cash", "Johnny",
                "sue@nashville.town", 1 );
        Employee j = checkEmpDao.save( johnny );
        Integer sid = j.getEmployeeid();
        try ( DAO<Integer, Employee> edao = daof.createDao( Employee.class );
                TransactionToken tok = edao.startTransaction(); ) {
            Employee sj = edao.get( sid ).get();
            sj.setFirstname( "Sue" );

            System.out.println( "Short appearance of Sue: " + sj );
            Employee sue = edao.update( sj );
            assertFalse( "connection still open?", ( (PGTransactionToken) tok ).getConnection().isClosed() );
            System.out.println( "newJohn = " + sue );
            assertEquals( "new name", "Sue", sue.getFirstname() );
            tok.commit();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        j = checkEmpDao.get( sid ).get();
        assertEquals( "Nashville", "Sue", j.getFirstname() );
        checkEmpDao.delete( j );
         
        //fail( "testUpdate not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetByLastName() throws SQLException {
        //DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        try ( DAO<Integer, Employee> edao = daof.createDao( Employee.class );
                TransactionToken tok = edao.startTransaction(); ) {
            Collection<Employee> allPuks = edao.getByColumnValues( "lastname","Puk");
            assertEquals(1,allPuks.size());
            Employee firstPuk = allPuks.iterator().next();
            
            assertEquals("Hi again piet, going through a transaction","Piet",firstPuk.getFirstname());
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        //fail( "testGetByLastName not yet implemented. Review the code and comment or delete this line" );
    }
}
