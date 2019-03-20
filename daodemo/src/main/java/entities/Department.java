package entities;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.COMMENTS;
import nl.fontys.sebivenlo.dao.Entity2;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class Department implements Entity2<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer departmentid;
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

    public void setDepartmentid( Integer departmentId ) {
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
          //0123456789012345678901234567890123456789012345678901234567890
"^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~]+@"
       // + "+@[a-zA-Z0-9](?:[a-zA-Z0-9]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9]{0,61}[a-zA-Z0-9])?)*$", CASE_INSENSITIVE | COMMENTS
    ).asPredicate();

    public final void setEmail( String email ) {
        if ( !EMAILTEST.test( email ) ) {
            throw new IllegalArgumentException( "not a valid email address: "
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

    @Override
    public Integer getNaturalId() {
        return this.departmentid;
    }

    @Override
    public ToIntFunction<Integer> idMapper() {
        return Integer::intValue;
    }

    @Override
    public String toString() {
        return "Department{" + "departmentid=" + departmentid + ", name=" + name + ", description=" + description + ", email=" + email + '}';
    }

    static Department implode( Object[] parts ) {
        int deptid = ( (Integer) parts[ 0 ] ).intValue();
        String name = (String) parts[ 1 ];
        String description = (String) parts[ 2 ];
        String email = (String) parts[ 3 ];
        return new Department( deptid, name, description, email );
    }

    Object[] explode() {
        return new Object[]{ departmentid, name, description, email };
    }

}
