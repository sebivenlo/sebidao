/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class EmployeeMapperTest {

    @Test
    public void testImplode() {

        EmployeeMapper em = new EmployeeMapper();
        Employee ip = em.implode( 1, "Puk", "Piet", "piet@fontys.nl", "sales" );

        Employee ep = new Employee( 1, "Puk", "Piet", "piet@fontys.nl", "sales" );
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
        Employee jan = new Employee( 3, "Klaassen", "Jan", "jan@google.com","fin" );

        EmployeeMapper em = new EmployeeMapper();
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
        EmployeeMapper em = new EmployeeMapper();
        Set<String> pfn = em.persistentFieldNames();
        assertTrue( "all mapped columns", pfn.contains( "employeeid" )
                && pfn.contains( "lastname" )
                && pfn.contains( "firstname" )
                && pfn.contains( "department" ) );
        //fail( "testPersistentFieldNames not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testKeyExtractor() {
        Employee jan = new Employee( 3, "Beton", "Bob","bob@truckers.org", "drivers" );
        EmployeeMapper em = new EmployeeMapper();
        assertTrue( "Bob was here", em.keyExtractor().apply( jan ).equals( 3 ) );

        //fail( "testKeyExtractor not yet implemented. Review the code and comment or delete this line" );
    }
}
