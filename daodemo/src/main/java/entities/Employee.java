package entities;

import java.util.Objects;
import java.util.function.ToIntFunction;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;
/**
 * Dutch naming.
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
    private String department;

    public Employee( Integer employeeid, String lastname, String firstname,
            String email,
            String department ) {
        this.employeeid = employeeid;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.department = department;
    }

    
    Employee( Object[] parts ) {
        this( ( int ) parts[ 0 ], ( String ) parts[ 1 ],
                ( String ) parts[ 2 ],( String ) parts[ 3 ], (String ) parts[ 4 ] );
    }

    Object[] asParts() {
        return new Object[] { getEmployeeid(), getLastname(),
            getFirstname(), getEmail(),getDepartment() };
    }

    public Employee( Integer employeeid ) {
        this.employeeid = employeeid;
    }

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
        final Employee other = ( Employee ) obj;
        return this.employeeid == other.employeeid;
    }

    @Override
    public String toString() {
        return "Employee{" + "employeeid=" + employeeid + ", lastname=" + lastname + ", firstname=" + firstname + ", email=" + email + ", department=" + department + '}';
    }


    public int getEmployeeid() {
        return employeeid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department= department;
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

    
}
