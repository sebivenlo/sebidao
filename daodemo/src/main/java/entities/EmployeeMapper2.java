package entities;

import java.util.function.Function;
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
        return e.asParts();
    }

    @Override
    public Employee implode( Object... parts ) {
        return new Employee(parts );
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::getEmployeeid;
    }
    
}
