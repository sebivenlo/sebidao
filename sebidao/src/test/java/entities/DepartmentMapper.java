package entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;
import nl.fontys.sebivenlo.dao.SimpleEntity;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DepartmentMapper extends AbstractMapper<String, Department> {

    public DepartmentMapper( Class<String> keyType, Class<Department> entityType ) {
        super( keyType, entityType );
    }

    @Override
    public Class<Department> entityType() {
        return Department.class;
    }

    @Override
    public Object[] explode( Department e ) {
        return e.asParts();
    }

    @Override
    public Department implode( Object[] parts ) {
        return new Department( parts );
    }

    @Override
    public Function<Department, String> keyExtractor() {
        return Department::getName;
    }
//    static final Set<String> FIELD_NAMES
//            = new LinkedHashSet<>( Arrays.asList(
//                    "departmentid",
//                    "name",
//                    "description",
//                    "email"
//            ) );
//
//    @Override
//    public Set<String> persistentFieldNames() {
//        return FIELD_NAMES;
//    }

}
