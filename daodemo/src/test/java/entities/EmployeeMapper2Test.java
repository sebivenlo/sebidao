package entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeMapper2Test {

    @Test
    public void testImplode() {

        EmployeeMapper2 em
                = new EmployeeMapper2( Integer.class, Employee.class );
        Employee ip = em.implode( 1, "Puk", "Piet", "piet@fontys.nl", "sales" );

        Employee ep
                = new Employee( 1, "Puk", "Piet", "piet@fontys.nl", "sales" );
        assertEqualsExtracting( "wrong employeeid", ep, ip,
                Employee::getEmployeeid );
        assertEqualsExtracting( "wrong firstname", ep, ip,
                Employee::getFirstname );
        assertEqualsExtracting( "wrong lastname", ep, ip,
                Employee::getLastname );
        assertEqualsExtracting( "wrong department", ep, ip,
                Employee::getDepartment );
    }

    @Test
    public void testExplode() {
        Employee jan = new Employee( 3, "Klaassen", "Jan", "jan@google.com",
                "unkown" );

        EmployeeMapper2 em
                = new EmployeeMapper2( Integer.class, Employee.class );
        Object[] parts = em.explode( jan );
        assertEquals( "id", parts[ 0 ], jan.getEmployeeid() );
        assertEquals( "ln", parts[ 1 ], jan.getLastname() );
        assertEquals( "fn", parts[ 2 ], jan.getFirstname() );
        assertEquals( "email", parts[ 3 ], jan.getEmail() );
        assertEquals( "did", parts[ 4 ], jan.getDepartment() );
        //fail( "testExplode not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetTableName() {
        assertEquals( "employee table", "employees", new EmployeeMapper().
                tableName() );
    }

    @Test
    public void testGetIdName() {
        Set<String> expected = new HashSet<>( Arrays.asList( "employeeid" ) );
        assertEquals( "employee id", expected, new EmployeeMapper().keyNames() );
    }

    private static <T, U> void assertEqualsExtracting( String msg, T expected,
            T actual, Function<? super T, ? extends U> extractor ) {
        assertEquals( msg, extractor.apply( actual ), extractor.
                apply( expected )
        );
    }

    @Test
    public void testPersistentFieldNames() {
        EmployeeMapper2 em
                = new EmployeeMapper2( Integer.class, Employee.class );
        Set<String> pfn = em.persistentFieldNames();
        assertTrue( "all mapped columns", pfn.contains( "employeeid" )
                && pfn.contains( "lastname" )
                && pfn.contains( "firstname" )
                && pfn.contains( "department" ) );
        //fail( "testPersistentFieldNames not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testKeyExtractor() {
        Employee jan = new Employee( 3, "Beton", "Bob", "bob@truckers.org", "unknown" );
        EmployeeMapper2 em
                = new EmployeeMapper2( Integer.class, Employee.class );
        assertTrue( "Bob was here", em.keyExtractor().apply( jan ).equals( 3 ) );

        //fail( "testKeyExtractor not yet implemented. Review the code and comment or delete this line" );
    }
}
