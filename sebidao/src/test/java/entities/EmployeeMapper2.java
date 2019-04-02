package entities;

import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class EmployeeMapper2 extends AbstractMapper<Integer,Employee> {

    public EmployeeMapper2( Class<Employee> entityType, Class<Integer> keyType ) {
        super( entityType, keyType );
    }

    @Override
    public Object[] explode( Employee e ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public Employee implode( Object[] parts ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

   
}
