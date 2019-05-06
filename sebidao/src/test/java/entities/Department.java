package entities;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.Generated;
import nl.fontys.sebivenlo.dao.ID;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Department implements Entity2<String> {

    private static final long serialVersionUID = 1L;
    @ID( generated = false )
    private String name;

    private String description;
    private String email;
    @Generated
    private Integer departmentid;

    public Department( Integer departmentId ) {
        this.departmentid = departmentId;
    }

    public Department( String name, String desciption,
            String email, Integer departmentid ) {
        this.name = name;
        this.description = desciption;
        setEmail( email );
        this.departmentid = departmentid;
    }

    public Department( Object[] parts ) {
        this( (String) parts[ 0 ], (String) parts[ 1 ],
                (String) parts[ 2 ], (Integer) parts[ 3 ] );

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
//        if ( !EMAILTEST.test( email ) ) {
//            throw new IllegalArgumentException( "not a valid email address"
//                    + email );
//        }
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
    public String getNaturalId() {
        return name;
    }

    Object[] asParts() {
        return new Object[]{ getName(), getDescription(),
            getEmail(), getDepartmentid() };
    }

}
