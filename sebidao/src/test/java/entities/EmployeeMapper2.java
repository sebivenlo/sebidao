package entities;

import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class EmployeeMapper2 extends AbstractMapper<Integer,Employee> {

    public EmployeeMapper2(  Class<Integer> keyType ,Class<Employee> entityType) {
        super( keyType,entityType );
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
