package entities;

import nl.fontys.sebivenlo.dao.pg.PGDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
//@Ignore
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class EmployeeDaoTest {

    static PGDataSource ds = PGDataSource.DATA_SOURCE;

    @Before
    public void setupData() throws Exception {
        loadDatabase();
    }
    EmployeeDao edao = new EmployeeDao( ds );

    @After
    public void cleanup() {

    }

    @Test
    public void test00Size() {
        int size = edao.size();
        assertEquals( "tests start out with one element", 1, size );

//        fail( "test method test00Size reached its end, you ca remove this line when you aggree." );
    }

    @Test
    public void test01Get() {
        int lastId = edao.lastId();
        System.out.println( "lastId = " + lastId );
        Optional<Employee> e = edao.get( lastId );

        assertTrue( "got an employee", e.isPresent() );
        assertEquals( "It's Piet", "Piet", e.get().getFirstname() );
        // fail( "testGet not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test02GetAll() {
        Collection<Employee> el = edao.getAll();
        System.out.println( "el=" + el );
        assertEquals( 1, el.size() );
        assertEquals( "Marvel: it is Piet again", "Piet", el.iterator().next().
                getFirstname() );
        //fail( "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test03Delete() {
        Employee dummy = new Employee( 1 );
        edao.delete( dummy ); // will drop piet
        System.out.println();
        Optional<Employee> e = edao.get( 1 );
        assertFalse( "sorry piet", e.isPresent() );

        // fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void test04Create() {
        Collection<Employee> el = edao.getAll();
        int preSize = el.size();
        System.out.println( "el = " + el);
        System.out.println( "preSize = " + preSize );
        edao.save( JAN );
        int postSize = edao.getAll().size();
        assertEquals( "no Jan?", 1 + preSize, postSize );

        // fail( "testCreate not yet implemented. Review the code and comment or delete this line" );
    }
    private static final Employee JAN = new Employee( 0, "Klaassen", "Jan",
            "jan@example.com", 1 );

//    @Ignore
    @Test
    public void test05Update() {
        Employee savedJan = edao.save( JAN );
        assertNotNull( "should have a jan", savedJan );
        assertTrue( "proper id", savedJan.getId() != 0 );

        //fail( "test05Update not yet implemented. Review the code and comment or delete this line" );
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
