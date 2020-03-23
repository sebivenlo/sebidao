package entities;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.COMMENTS;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.Generated;
import nl.fontys.sebivenlo.dao.ID;
import pgtypes.Email;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Department implements Entity2<String> {

    private static final long serialVersionUID = 1L;
    private String description;
    @ID(generated = false)
    private String name;
    private Email email;
    @Generated
    private Integer departmentid;

    public Department( String name ) {
        this.name = name;
    }


    public Department( String name, String desciption, String email ) {
        this.name = name;
        this.description = desciption;
        try {
            this.email= new Email(email) ;
        } catch ( SQLException ex ) {
            Logger.getLogger( Department.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public Department( String description, String name, Email email, Integer departmentid ) {
        this.description = description;
        this.name = name;
        this.email = email;
        this.departmentid = departmentid;
    }

    
    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }


    @Override
    public int hashCode() {
        return Objects.hash( this.name );
    }

    public Email getEmail() {
        return email;
    }

    private static final Predicate<String> EMAILTEST = Pattern.compile(
            //0123456789012345678901234567890123456789012345678901234567890
            "^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~]+@"
    // + "+@[a-zA-Z0-9](?:[a-zA-Z0-9]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9]{0,61}[a-zA-Z0-9])?)*$", CASE_INSENSITIVE | COMMENTS
    ).asPredicate();

    public final void setEmail( String email ) {
        if ( !EMAILTEST.test( email ) ) {
            throw new IllegalArgumentException( "not a valid email address: "
                    + email );
        }
        try {
            this.email = new Email(email);
        } catch ( SQLException ex ) {
            Logger.getLogger( Department.class.getName() ).log( Level.SEVERE, null, ex );
        }
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
        final Department other = (Department) obj;
        return this.name.equals( other.name );
    }

    @Override
    public String getNaturalId() {
        return this.name;
    }

    
    @Override
    public String toString() {
        return "Department{ name=" + name + ", description=" + description + ", email=" + email + '}';
    }

    static Department implode( Object[] parts ) {
        String name = (String) parts[ 0 ];
        String description = (String) parts[ 1 ];
        String email = (String) parts[ 2 ];
        return new Department(  name, description, email );
    }

    Object[] explode() {
        return new Object[]{ description, name, email,departmentid };
    }

    @Override
    public int getId() {
        return departmentid;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    
    
}
