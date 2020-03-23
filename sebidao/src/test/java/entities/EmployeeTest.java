package entities;

import static entities.Email.email;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeTest {

    @Test
    public void testFromParts() {
        Object[] parts = new Object[]{
            1, "Puk", "Piet", email( "piet@student.fontys.nl"), 42, true, now()
        };

        Employee employee = new Employee( parts );

        Integer id = employee.getId();
        String lastname = employee.getLastname();
        String firstname = employee.getFirstname();
        int dept = employee.getDepartmentid();
        assertThat( id.intValue() ).isEqualTo( 1 );
        assertThat( lastname ).isEqualTo( "Puk" );
        assertThat( firstname ).isEqualTo( "Piet" );
        assertThat( dept ).isEqualTo( 42 );

        // fail( "testFromParts not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testToParts() {
        Employee emp = new Employee( 0 );
        emp.setEmployeeid( 1 );
        emp.setFirstname( "Piet" );
        emp.setLastname( "Puk" );
        String es="p.puk@outlook.com";
        emp.setEmail( email(es) );
        emp.setDepartmentid( 42 );

        emp.setDob( of( 1953, 9, 15 ) );

        Object[] part = emp.asParts();
        assertThat( ( (Integer) part[ 0 ] ).intValue() ).as( "id" )
                .isEqualTo( 1 );
        assertThat( part[ 1 ] ).as( "last" ).isEqualTo( "Puk" );
        assertThat( part[ 2 ] ).as( "first" ).isEqualTo( "Piet" );
        assertThat( part[ 3 ] ).as( "email" ).extracting( x -> x.toString()).isEqualTo(es );
        assertThat( ( (Integer) part[ 4 ] ).intValue() ).as( "dept" )
                .isEqualTo( 42 );

        //fail( "testToParts not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testCoverageLeftOvers() {
        Employee emp1 = new Employee( 23 );
        emp1.setFirstname( "Piet" );
        Employee emp2 = new Employee( 23 );

        emp2.setFirstname( "Jan" );

        assertThat( emp2 ).isEqualTo( emp1 );

        assertThat( emp2.toString() ).contains( "Jan" );
        assertThat( emp1.hashCode() ).as( "hashCode should be equal" )
                .isEqualTo( emp2.hashCode() );
        //fail( "testCoverageLeftOvers not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testCoverEquals() {
        Employee emp = new Employee( 56 );
        emp.setLastname( "Henk" );

        assertThat( emp ).isEqualTo( emp );
        assertThat( emp.equals( null ) ).isFalse();
        assertThat( emp.equals( "Hank" ) ).isFalse();

        Employee emp2 = new Employee( 57 );
        assertThat( emp2.equals( emp )).isFalse();

        //fail( "testCoverEquals not yet implemented. Review the code and comment or delete this line" );
    }
}
