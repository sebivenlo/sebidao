package entities;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import nl.fontys.sebivenlo.dao.Generated;
import nl.fontys.sebivenlo.dao.ID;
import nl.fontys.sebivenlo.dao.SimpleEntity;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Department implements SimpleEntity {

    private static final long serialVersionUID = 1L;
    @Generated
    private Integer departmentid;
    @ID( generated = false )
    private String name;

    private String description;
    private String email;

    public Department( Integer departmentId ) {
        this.departmentid = departmentId;
    }

    public Department( int departmentid, String name, String desciption, String email ) {
        this.departmentid = departmentid;
        this.name = name;
        this.description = desciption;
        setEmail( email );
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentis( Integer departmentId ) {
        this.departmentid = departmentId;
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
    public int getId() {
        return departmentid;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.departmentid;
        return hash;
    }

    public String getEmail() {
        return email;
    }

    private static final Predicate<String> EMAILTEST = Pattern.compile(
            "'^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+"
            + "@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}"
            + "[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}"
            + "[a-zA-Z0-9])?)*$", CASE_INSENSITIVE
    ).asPredicate();

    public final void setEmail( String email ) {
        if ( !EMAILTEST.test( email ) ) {
            throw new IllegalArgumentException( "not a valid email address"
                    + email );
        }
        this.email = email;
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
        return this.departmentid == other.departmentid;
    }

}
