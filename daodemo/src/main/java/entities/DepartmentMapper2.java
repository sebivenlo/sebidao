package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class DepartmentMapper2 extends AbstractMapper<String, Department> {

    public DepartmentMapper2( Class<String> keyType,
            Class<Department> entityType ) {
        super( keyType, entityType );
    }

    @Override
    public Object[] explode( Department e ) {
        return e.explode();
    }

    @Override
    public Department implode( Object[] parts ) {
        return new Department( (String) parts[ 0 ], (String) parts[ 1 ], (String) parts[ 2 ] );
    }

    @Override
    public Function<Department, String> keyExtractor() {
        return Department::getEmail;
    }

}
