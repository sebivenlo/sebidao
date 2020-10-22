package entities;

import entities.*;
import static entities.Email.email;
import java.time.LocalDate;
import java.util.Objects;
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
    private Email email;
    private Integer departmentid;
    private Boolean available;
    private LocalDate dob;

    public Employee( Integer employeeid, String lastname, String firstname, Email email, Integer departmentid, Boolean available, LocalDate dob ) {
        this.employeeid = employeeid;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.departmentid = departmentid;
        this.available = available;
        this.dob = dob;
    }

    public Employee( Integer employeeid, String lastname, String firstname,
            String email, Integer departmentid, Boolean available, LocalDate dob ) {
        this.employeeid = employeeid;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email( email );
        this.departmentid = departmentid;
        this.available = available;
        this.dob = dob;
    }

    public Employee( Integer employeeid, String lastname, String firstname,
            Email email,
            Integer departmentid ) {
        this( employeeid, lastname, firstname, email, departmentid, true, LocalDate.now() );
    }

    public Employee( Integer employeeid, String lastname, String firstname,
            String email,
            Integer departmentid ) {
        this( employeeid, lastname, firstname, email( email ), departmentid, true, LocalDate.now() );
    }

    Employee( Object[] parts ) {
        this( (Integer) parts[ 0 ], (String) parts[ 1 ],
                (String) parts[ 2 ], (Email) parts[ 3 ], (Integer) parts[ 4 ], (Boolean) parts[ 5 ], (LocalDate) parts[ 6 ] );
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

    public Integer getEmployeeid() {
        return employeeid;
    }

    public int getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid( int departmentid ) {
        this.departmentid = departmentid;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail( Email email ) {
        this.email = email;
    }

    @Override
    public Integer getNaturalId() {
        return this.employeeid;
    }

    public Boolean getAvailable() {
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
