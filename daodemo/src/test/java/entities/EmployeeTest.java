package entities;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeTest {

    @Test
    public void testFromParts() {
        Object[] parts = new Object[] {
            1, "Puk", "Piet","piet@student.fontys.nl", "drivers"
        };

        Employee employee = new Employee( parts );

        Integer id = employee.getId();
        String lastname = employee.getLastname();
        String firstname = employee.getFirstname();
        String dept = employee.getDepartment();
        assertEquals( "id", 1, id.intValue() );
        assertEquals( "last", "Puk", lastname );
        assertEquals( "first", "Piet", firstname );
        assertEquals( "first", "drivers", dept );

        // fail( "testFromParts not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testToParts() {
        Employee emp = new Employee( 0 );
        emp.setEmployeeid( 1 );
        emp.setFirstname( "Piet" );
        emp.setLastname( "Puk" );
        emp.setEmail("p.puk@outlook.com" );
        emp.setDepartment( "fin" );
        System.out.println( "emp = " + emp );
        Object[] part = emp.asParts();
        System.out.println( Arrays.toString( part ));
        assertEquals( "id", 1, ( ( Integer ) part[ 0 ] ).intValue() );
        assertEquals( "last", "Puk", part[ 1 ] );
        assertEquals( "first", "Piet", part[ 2 ] );
        assertEquals( "email", "p.puk@outlook.com", part[ 3 ] );
        assertEquals( "dept", "fin",  part[ 4 ] );

        //fail( "testToParts not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testCoverageLeftOvers() {
        Employee emp1 = new Employee( 23 );
        emp1.setFirstname( "Piet" );
        Employee emp2 = new Employee( 23 );

        emp2.setFirstname( "Jan" );

        assertEquals( "equals by id", emp1, emp2 );

        assertTrue( "Proper name in toString", emp2.toString().contains( "Jan" ) );
        assertEquals( "hashCode should be equal", emp1.hashCode(), emp2.
                hashCode() );
        //fail( "testCoverageLeftOvers not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testCoverEquals() {
        Employee emp = new Employee( 56 );
        emp.setLastname( "Henk" );

        assertEquals( "self", emp, emp );
        assertFalse( emp.equals( null ) );
        assertFalse( emp.equals( "Hank" ) );

        Employee emp2 = new Employee( 57 );
        assertNotEquals( emp2, emp );
        
        //fail( "testCoverEquals not yet implemented. Review the code and comment or delete this line" );
    }
}
