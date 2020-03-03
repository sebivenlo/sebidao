package entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.Assert;
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

    static PGDataSource ds = PGDataSource.DATA_SOURCE;
    static PGDAOFactory daof;

    @BeforeClass
    public static void setupClass() {
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper() );
    }

    @Before
    public void setupData() throws Exception {
        loadDatabase();
    }

    @After
    public void cleanup() {

    }

    @Test
    public void t00Size() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );

        int size = edao.size();
        assertEquals( "ts start out with one element", 1, size );

//        fail( "t method t00Size reached its end, you ca remove this line when you aggree." );
    }

    @Test
    public void t01Get() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int lastId = edao.lastId();
        System.out.println( "lastId = " + lastId );
        Optional<Employee> e = edao.get( lastId );

        assertTrue( "got an employee", e.isPresent() );
        assertEquals( "It's Piet", "Piet", e.get().getFirstname() );
        // fail( "tGet not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void t02GetAll() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        System.out.println( "el=" + el );
        assertEquals( 1, el.size() );
        assertThat( el ) // <4>
                .as( "no Piet?" ) // <5>
                .hasSize( 1 ) // <6>
                .extracting( Employee::getEmail).contains( "p.puk@vanderheiden.nl" ); // <7>
        //fail( "tGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void t03Delete() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee dummy = new Employee( 1 );
        edao.delete( dummy ); // will drop piet
        System.out.println();
        Optional<Employee> e = edao.get( 1 );
        assertThat( e ).isEmpty();

        // fail( "tDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void t04Create() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Collection<Employee> el = edao.getAll();
        int preSize = el.size();
        System.out.println( "el = " + el );
        System.out.println( "preSize = " + preSize );
        edao.save( JAN );
        int postSize = edao.getAll().size();
        assertEquals( "no Jan?", 1 + preSize, postSize );

        // fail( "tCreate not yet implemented. Review the code and comment or delete this line" );
    }
    private static final Employee JAN = new Employee( 0, "Klaassen", "Jan",
                                                      "jan@example.com", "sales" );

//    @Ignore
    @Test
    public void t05Update() {
        DAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee savedJan = edao.save( JAN );
        assertNotNull( "should have a jan", savedJan );
        assertTrue( "proper id", savedJan.getId() != 0 );

        //fail( "t05Update not yet implemented. Review the code and comment or delete this line" );
    }

//    @Ignore
    @Test
    public void tUpdateEmail() {
        DAO<Integer, Employee> eDao = daof.createDao( Employee.class );
        Employee savedJan = eDao.save( JAN ); // <1>
        String nemail = "janklaassen@outlook.com";
        savedJan.setEmail( nemail ); // <2>

        eDao.update( savedJan ); // <3>
        Employee updatedJan = eDao.get( savedJan.getEmployeeid() ).get(); // <4>

        assertThat( savedJan ).as( "see if email update worked" ) // <5>
                .extracting( j -> j.getEmail() )
                .isEqualTo( nemail );
//        Assert.fail( "tMethod reached it's and. You will know what to do." );
    }

    private static void loadDatabase() throws IOException, SQLException {
        String ddl
                = Files.lines( Paths.get( "dbscripts/newpiet.sql" ) )
                        .filter( l -> !l.startsWith( "--" ) )
                        .collect( joining( System.lineSeparator() ) );
        doDDL( ddl );
    }

    private static void doDDL( String ddl ) throws
            SQLException {
        //System.out.println( ddl + ";\n" );
        try ( Connection con = ds.getConnection() ) {
            con.prepareStatement( ddl ).execute();
        }
    }
}
