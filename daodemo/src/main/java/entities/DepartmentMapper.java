package entities;

import entities.Department;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.Mapper;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class DepartmentMapper implements Mapper<String, Department> {

    @Override
    public Class<Department> entityType() {
        return Department.class;
    }

    @Override
    public Object[] explode( Department e ) {
        return e.explode();
    }

    @Override
    public Department implode( Object[] parts ) {
        return Department.implode( parts );
    }

    @Override
    public Function<Department, String> keyExtractor() {
        return Department::getEmail;
    }
    ;
    
    static final Set<String> FIELD_NAMES
            = new LinkedHashSet<>( Arrays.asList(
                     "name", "description", "email" ) );

    @Override
    public Set<String> persistentFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public String idName() {
        return "email";
    }

    @Override
    public boolean generateKey() {
        return false;
    }

    Set<String> keyNames = new LinkedHashSet<>(asList("email"));
    @Override
    public Set<String> keyNames() {
        return keyNames;
    }

}
