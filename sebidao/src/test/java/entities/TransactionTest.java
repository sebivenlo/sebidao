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
import static org.assertj.core.api.Assertions.*;
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
        daof.registerMapper( Employee.class, new EmployeeMapper2() );
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
            assertThat( savedHenk.getEmployeeid() != 0 )
                    .as( "real employee id" ).isTrue();
            tok.rollback();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
        int afterSize = checkEmpDao.size();

        assertThat( beforeSize )
                .as( "size changed, although transaction rolled back " )
                .isEqualTo( afterSize );
        //fail( "testAddDeptWithBoss not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testAddDeptWithBossRollBack() {
        Department engineering = new Department( "Engineering",
                                                 "Where value creation happens", "dilbert@example.com", 0 );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                                         "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();

        try (
                DAO<String, Department> ddao = daof
                        .createDao( Department.class );
                TransactionToken tok = ddao.startTransaction();
                DAO<Integer, Employee> edao = daof
                        .createDao( Employee.class, tok ); ) {
                    Department save = ddao.save( engineering );
                    int depno = save.getDepartmentid();
                    dilbert.setDepartmentid( depno );
                    edao.save( dilbert );
                    int tempSize = edao.size();
                    assertThat( tempSize ).as( "temp emp size" )
                            .isEqualTo( empSize + 1 );
                    tok.rollback();
                } catch ( Exception ex ) {
                    fail( "unexpected exception " + ex );
                    Logger.getLogger( TransactionTest.class.getName() ).
                            log( Level.SEVERE, null, ex );
                }

                int newDepSize = checkDepDao.size();
                int newEmpSize = checkEmpDao.size();

                assertThat( newDepSize ).isEqualTo( deptSize );
                assertThat( newEmpSize ).isEqualTo( empSize );
//        fail( "testMethod not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testAddDeptWithBossCommit() {
        Department engineering = new Department( "Engineering",
                                                 "Where value creation happens", "dilbert@example.com", null );
        Employee dilbert = new Employee( 0, "O'Hana", "Dilbert",
                                         "dilbert@example.com", 1 );
        int deptSize = checkDepDao.size();
        int empSize = checkEmpDao.size();
        Department savedDept = null;
        Employee savedDilbert = null;

        try (
                DAO<String, Department> ddao = daof
                        .createDao( Department.class );
                TransactionToken tok = ddao.startTransaction();
                DAO<Integer, Employee> edao = daof
                        .createDao( Employee.class, tok ); ) {
                    System.out.println( "tok = " + tok );
                    savedDept = ddao.save( engineering );
                    Integer depno = savedDept.getDepartmentid();
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
                assertThat( newDepSize == deptSize + 1
                        && newEmpSize == empSize + 1 )
                        .as( "both table should have grown" ).isTrue();
//        fail( "testAddDeptWithBossCommit not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetAll() {

        try (
                DAO<Integer, Employee> edao = daof.createDao( Employee.class ); ) {
            TransactionToken tok = edao.startTransaction();
            Collection<Employee> all = edao.getAll();
            Connection connection = ( (PGTransactionToken) tok ).getConnection();
            assertThat( connection.isClosed() )
                    .as( "connection should stay open" )
                    .isFalse();
            System.out.println( "all = " + all );
            int size = edao.size();
            System.out.println( "size = " + size );
            assertThat( all.size() ).as( "two calls same size" )
                    .isEqualTo( size );
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

            assertThat( optionalPiet ).as( "do we have anything Piet" )
                    .isPresent();
            Connection connection = ( (PGTransactionToken) tok ).getConnection();
            assertThat( connection.isClosed() )
                    .as( "connection should stay open" )
                    .isFalse();
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
            Connection usedCon = ( (PGTransactionToken) tok ).getConnection();
            assertThat( usedCon.isClosed() )
                    .as( "connection still open?" )
                    .isFalse();
            System.out.println( "newJohn = " + sue );
            assertThat( sue.getFirstname() ).as("last field is dob").isEqualTo( "Sue" );
            tok.commit();
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        j = checkEmpDao.get( sid ).get();
        assertThat( j.getFirstname() ).isEqualTo( "Sue" );
        checkEmpDao.delete( j );

        //fail( "testUpdate not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetByLastName() throws SQLException {
        //DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        try ( DAO<Integer, Employee> edao = daof.createDao( Employee.class );
                TransactionToken tok = edao.startTransaction(); ) {
            Collection<Employee> allPuks = edao
                    .getByColumnValues( "lastname", "Puk" );
            assertThat( allPuks ).hasSize( 1 );
            Employee firstPuk = allPuks.iterator().next();

            assertThat( firstPuk.getFirstname() )
                    .as( "Hi again piet, going through a transaction" )
                    .isEqualTo( "Piet" );
        } catch ( Exception ex ) {
            fail( "unexpected exception " + ex );
            Logger.getLogger( TransactionTest.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

        //fail( "testGetByLastName not yet implemented. Review the code and comment or delete this line" );
    }
}
