package entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.Mapper;

public class EmployeeMapper implements Mapper<Integer, Employee> {

    @Override
    public Object[] explode( Employee e ) {
        return e.asParts();
    }

    @Override
    public Employee implode( Object... parts ) {
        return new Employee( parts );
    }

    @Override
    public Set<String> persistentFieldNames() {
        return FIELD_NAMES;
    }
    
    
    static final Set<String> FIELD_NAMES
            = new LinkedHashSet<>( Arrays.asList(
                    "employeeid",
                    "lastname",
                    "firstname",
                    "email",
                    "departmentid"
            ) );

    private static final Set<String> KEYS = new HashSet<>( Arrays.asList(
            "employeeid" ) );

    @Override
    public Set<String> keyNames() {
        return KEYS;
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::getId;
    }

    @Override
    public Class<Employee> entityType() {
        return Employee.class;
    }
}
