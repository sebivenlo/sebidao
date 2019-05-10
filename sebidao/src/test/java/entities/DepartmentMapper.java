package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DepartmentMapper extends AbstractMapper<String, Department> {

    public DepartmentMapper( ) {
        super( String.class, Department.class );
    }

//    @Override
//    public Class<Department> entityType() {
//        return Department.class;
//    }
//
//    @Override
//    public Object[] explode( Department e ) {
//        return e.asParts();
//    }

//    @Override
//    public Department implode( Object[] parts ) {
//        return new Department( parts );
//    }

    @Override
    public Function<Department, String> keyExtractor() {
        return Department::getName;
    }
}
