package entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class EmployeeMapper2 extends AbstractMapper<Integer, Employee> {

    public EmployeeMapper2() {
        super( Integer.class, Employee.class );
    }

//    @Override
//    public Object[] explode( Employee e ) {
//        return e.asParts();
//    }

//    @Override
//    public Employee implode( Object... parts ) {
//        return new Employee( parts );
//    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::getEmployeeid;
    }

}
