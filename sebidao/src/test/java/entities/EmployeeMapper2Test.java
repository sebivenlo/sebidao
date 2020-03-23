package entities;

import static entities.Email.email;
import static java.time.LocalDate.now;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeMapper2Test {

    @Test
    public void testImplode() {

        EmployeeMapper2 em = new EmployeeMapper2();
        Employee ip = em.implode( 1, "Puk", "Piet", email( "piet@fontys.nl"), 10, true );

        Employee ep = new Employee( 1, "Puk", "Piet", email("piet@fontys.nl"), 10, false, now() );
        assertEqualsExtracting( "wrong employeeid", ep, ip,
                                Employee::getEmployeeid );
        assertEqualsExtracting( "wrong firstname", ep, ip,
                                Employee::getFirstname );
        assertEqualsExtracting( "wrong lastname", ep, ip,
                                Employee::getLastname );
        assertEqualsExtracting( "wrong department", ep, ip,
                                Employee::getDepartmentid );
    }

    @Test
    public void testExplode() {
        Employee jan = new Employee( 3, "Klaassen", "Jan", email("jan@google.com"), 42 );

        EmployeeMapper2 em = new EmployeeMapper2();
        Object[] parts = em.explode( jan );
        assertThat( jan.getEmployeeid() ).isEqualTo( parts[ 0 ] );
        assertThat( jan.getLastname() ).isEqualTo( parts[ 1 ] );
        assertThat( jan.getFirstname() ).isEqualTo( parts[ 2 ] );
        assertThat( jan.getEmail() ).isEqualTo( parts[ 3 ] );
        assertThat( jan.getDepartmentid() ).isEqualTo( (int) parts[ 4 ] );
        //fail( "testExplode not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetTableName() {
        assertThat( new EmployeeMapper2().tableName() ).isEqualTo( "employees" );
    }

    @Test
    public void testGetIdName() {
        Set<String> expected = new HashSet<>( Arrays.asList( "employeeid" ) );
        assertThat( new EmployeeMapper2().keyNames() ).isEqualTo( expected );
    }

    private static <T, U> void assertEqualsExtracting( String msg, T expected,
                                                       T actual, Function<? super T, ? extends U> extractor ) {
        assertThat( extractor.apply( actual ) ).as( msg ).isEqualTo( extractor.
                apply( expected )
        );
    }

    @Test
    public void testPersistentFieldNames() {
        EmployeeMapper2 em = new EmployeeMapper2();
        Set<String> pfn = em.persistentFieldNames();
        assertThat( pfn ).contains(
                "employeeid",
                "lastname",
                "firstname",
                "departmentid" );
        //fail( "testPersistentFieldNames not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testKeyExtractor() {
        Employee bob = new Employee( 3, "Beton", "Bob", email("bob@truckers.org"), 42 );
        EmployeeMapper2 em = new EmployeeMapper2();
        assertThat( em.keyExtractor().apply( bob )).isEqualTo(3);

        //fail( "testKeyExtractor not yet implemented. Review the code and comment or delete this line" );
    }
}
