package entities;

import static entities.DBTestHelpers.daof;
import static entities.DBTestHelpers.ds;
import static entities.DBTestHelpers.loadDatabase;
import static entities.Email.email;
import java.util.Collection;
import java.util.Optional;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.junit.After;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
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
//        daof = new PGDAOFactory( ds ).registerInMarshaller( Email.class, Email::new );

        daof.registerMapper( Employee.class, new EmployeeMapper2() );
        daof.registerPGMashallers( Email.class, Email::new, x -> PGDAOFactory.pgobject( "citext", x ) );

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
        assertThat( size ).as( "tests start out with one element" )
                .isEqualTo( 1 );

        // fail( "test method test00Size reached its end, you ca remove this line when you aggree." );
    }

    @Test
    public void test01Get() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int lastId = edao.lastId();
        Optional<Employee> e = edao.get( lastId );

        assertThat( e.isPresent() ).as( "got an employee" ).isTrue();
        assertThat( e.get().getFirstname() ).as( "It's Piet" )
                .isEqualTo( "Piet" );
        // fail( "testGet not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test02GetAll() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        assertThat( el.size() ).isEqualTo( 1 );
        assertThat( el.iterator().next().getFirstname() ).isEqualTo( "Piet" );
        //fail( "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test03Delete() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee dummy = new Employee( 1 );
        edao.delete( dummy ); // will drop piet
        Optional<Employee> oe = edao.get( 1 );
        assertThat( oe ).isEmpty();

        // fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test04Create() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        int preSize = el.size();
        Employee savedJan = edao.save( JAN );
        int postSize = edao.getAll().size();
        assertThat( postSize ).isEqualTo( 1 + preSize );
        edao.delete( savedJan );
        // fail( "testCreate not yet implemented. Review the code and comment or delete this line" );
    }
    private static final Employee JAN = new Employee( 0, "Klaassen", "Jan",
            email( "jan@example.com" ), 1 );

    @Test
    public void test05Update() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee savedJan = edao.save( JAN );
        assertThat( savedJan ).isNotNull();
        assertThat( savedJan.getId() ).isNotEqualTo( 0 );
        System.out.println( "savedJan = " + savedJan );
        savedJan.setEmail( email( "janklaassen@outlook.com" ) );
        edao.update( savedJan ); // ignore result for now
        Employee updatedJan = edao.get( savedJan.getEmployeeid() ).get();

        assertThat( savedJan.getEmail() ).extracting( e->e.toString() )
                .isEqualTo( "janklaassen@outlook.com" );
        // fail( "test05Update not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test00GetByKeyValues() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        // should get default piet.
        Employee savedJan = edao.save( JAN );
        Collection<Employee> col = edao.getByColumnValues( "email", JAN.
                getEmail() );
        assertThat( col.isEmpty() ).isFalse();
        Employee firstEmployee = col.iterator().next();
        assertThat( firstEmployee.getFirstname() ).isEqualTo( "Jan" );
        edao.delete( savedJan );
        //fail( "test method testGetByKeyValues reached its end, you can remove this line when you aggree." );
    }

    @Test( expected = DAOException.class )
    public void testSaveUniqueViolation() {
        Employee jean = new Employee( 0, "Klaassen", "Jean",
                email( "jan@example.com" ), 1 );
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        // should get default piet.
        Employee savedJean = edao.save( JAN );
        Employee savedJan = edao.save( jean );

        // fail( "testSaveUniqueViolation not yet implemented. Review the code and comment or delete this line" );
    }
}
