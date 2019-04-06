package entities;

import static entities.DBTestHelpers.daof;
import static entities.DBTestHelpers.ds;
import static entities.DBTestHelpers.loadDatabase;
import java.util.Collection;
import java.util.Optional;
import net.bytebuddy.agent.builder.AgentBuilder;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
//@Ignore
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class EmployeeDaoTest {

    @BeforeClass
    public static void setupClass() {
        loadDatabase();
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper() );
    }
    //DAO<Integer, Employee> edao;

    @Before
    public void setupData() throws Exception {
    }

    @After
    public void cleanup() {

    }

    @Test
    public void test00Size() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int size = edao.size();
        assertEquals( "tests start out with one element", 1, size );

        // fail( "test method test00Size reached its end, you ca remove this line when you aggree." );
    }

    @Test
    public void test01Get() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int lastId = edao.lastId();
        Optional<Employee> e = edao.get( lastId );

        assertTrue( "got an employee", e.isPresent() );
        assertEquals( "It's Piet", "Piet", e.get().getFirstname() );
        // fail( "testGet not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test02GetAll() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        assertEquals( 1, el.size() );
        assertEquals( "Marvel: it is Piet again", "Piet", el.iterator().next().
                getFirstname() );
        //fail( "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test03Delete() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee dummy = new Employee( 1 );
        edao.delete( dummy ); // will drop piet
        Optional<Employee> e = edao.get( 1 );
        assertFalse( "sorry piet", e.isPresent() );

        // fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test04Create() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        int preSize = el.size();
        Employee savedJan = edao.save( JAN );
        int postSize = edao.getAll().size();
        assertEquals( "no Jan?", 1 + preSize, postSize );
        edao.delete( savedJan );
        // fail( "testCreate not yet implemented. Review the code and comment or delete this line" );
    }
    private static final Employee JAN = new Employee( 0, "Klaassen", "Jan",
            "jan@example.com", 1 );

    @Test
    public void test05Update() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee savedJan = edao.save( JAN );
        assertNotNull( "should have a jan", savedJan );
        assertTrue( "proper id", savedJan.getId() != 0 );
        System.out.println( "savedJan = " + savedJan );
        savedJan.setEmail( "janklaassen@outlook.com" );
        edao.update( savedJan ); // ignore result for now
        Employee updatedJan = edao.get( savedJan.getEmployeeid() ).get();

        assertEquals( "see if email update worked", savedJan.getEmail(),
                updatedJan.
                        getEmail() );
        // fail( "test05Update not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test00GetByKeyValues() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        // should get default piet.
        Employee savedJan = edao.save( JAN );
        Collection<Employee> col = edao.getByColumnValues( "email", JAN.
                getEmail() );
        assertFalse( col.isEmpty() );
        Employee firstEmployee = col.iterator().next();
        assertEquals( "Hi Piet", "Jan", firstEmployee.getFirstname() );
        edao.delete( savedJan );

        //fail( "test method testGetByKeyValues reached its end, you can remove this line when you aggree." );
    }

    @Test(expected = DAOException.class)
    public void testSaveUniqueViolation() {
        Employee jean = new Employee( 0, "Klaassen", "Jean",
                "jan@example.com", 1 );
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        // should get default piet.
        Employee savedJean = edao.save( JAN );
        Employee savedJan = edao.save( jean );
       
       // fail( "testSaveUniqueViolation not yet implemented. Review the code and comment or delete this line" );
    }
}
