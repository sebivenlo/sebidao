package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeMapper extends AbstractMapper<Integer, Employee> {

    public EmployeeMapper() {
        super( Integer.class, Employee.class );
    }

    @Override
    public Object[] explode( Employee e ) {
        return e.asParts();
    }

    @Override
    public Employee implode( Object... parts ) {
        return super.implode( parts ); //To change body of generated methods, choose Tools | Templates.
    }

    
//    @Override
//    public Employee implode( Object... parts ) {
//        return new Employee( parts );
//    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::getEmployeeid;
    }

}
