package entities;

import java.time.LocalDate;
import static java.time.LocalDateTime.now;
import java.util.Objects;
import java.util.function.ToIntFunction;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;

/**
 * Dutch naming. (fields all lower case.)
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Employee implements Entity2<Integer> {

    private static final long serialVersionUID = 1L;
    @ID
    private Integer employeeid;
    private String lastname;
    private String firstname;
    private String email;
    private int departmentid;
    private Boolean available;
    private LocalDate dob;

    public Employee( Integer employeeid, String lastname, String firstname, String email, int departmentid, Boolean available, LocalDate dob ) {
        this.employeeid = employeeid;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.departmentid = departmentid;
        this.available = available;
        this.dob = dob;
    }

    public Employee( int employeeid, String lastname, String firstname,
            String email,
            int departmentid ) {
        this( employeeid, lastname, firstname, email, departmentid, true, LocalDate.now() );
    }

    Employee( Object[] parts ) {
        this( (int) parts[ 0 ], (String) parts[ 1 ],
                (String) parts[ 2 ], (String) parts[ 3 ], (int) parts[ 4 ], (Boolean) parts[ 5 ], (LocalDate) parts[ 6 ] );
    }

    Object[] asParts() {
        return new Object[] {
            employeeid,
            lastname,
            firstname,
            email,
            departmentid,
            available,
            dob
        };
    }

    public Employee( Integer employeeid ) {
        this.employeeid = employeeid;
    }

    @Override
    public int getId() {
        return employeeid;
    }

    public void setEmployeeid( int id ) {
        this.employeeid = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname( String lastname ) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname( String firstname ) {
        this.firstname = firstname;
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.employeeid );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Employee other = (Employee) obj;
        return ( this.employeeid == null && other.employeeid == null )
                || this.employeeid.equals( other.employeeid );
    }

//    @Override
//    public String toString() {
//        return "Employees{" + "employeeid=" + employeeid + ", lastname="
//                + lastname + ", firstname=" + firstname
//                + ", email=" + email
//                + ", departmentid=" + departmentid + '}';
//    }
    @Override
    public String toString() {
        return "Employee{"
                + "employeeid=" + employeeid
                + ", lastname=" + lastname
                + ", firstname=" + firstname
                + ", email=" + email
                + ", departmentid=" + departmentid
                + ", available=" + available
                + ", dob=" + dob
                + '}';
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid( int departmentid ) {
        this.departmentid = departmentid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    @Override
    public Integer getNaturalId() {
        return this.employeeid;
    }

    private Boolean getAvailable() {
        return available;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob( LocalDate dob ) {
        this.dob = dob;
    }

    public void setEmployeeid( Integer employeeid ) {
        this.employeeid = employeeid;
    }

    public void setAvailable( Boolean available ) {
        this.available = available;
    }

}
