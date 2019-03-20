package entities;

import entities.Department;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.Mapper;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class DepartmentMapper implements Mapper<Integer, Department> {

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
    public Function<Department, Integer> keyExtractor() {
        return Department::getDepartmentid;
    }
    ;
    
    static final Set<String> FIELD_NAMES
            = new LinkedHashSet<>( Arrays.asList(
                    "departmentid", "name", "description", "email" ) );

    @Override
    public Set<String> persistentFieldNames() {
        return FIELD_NAMES;
    }

}
