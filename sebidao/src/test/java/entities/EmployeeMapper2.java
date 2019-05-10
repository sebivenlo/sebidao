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

    @Override
    public Object[] explode( Employee e ) {
        return e.asParts();
    }

    @Override
    public Employee implode( Object... parts ) {
        return new Employee( parts );
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::getEmployeeid;
    }

//    /**
//     * Get the table name for the entity.
//     *
//     * @return the name of the table in the database
//     */
//    default String tableName() {
//        return entityType().getSimpleName().toLowerCase() + 's';
//    }
//
//    /**
//     * Get the column name(s) for the key column(e), typically the forming
//     * primary key.
//     *
//     * Note that the minimal, but also default length of the returned array is
//     * one.
//     *
//     * @return the id column name
//     */
//    default Set keyNames() {
//        return new LinkedHashSet<>( asList( idName() ) );
//    }


//    /**
//     * Get the name of the natural key of this relation, if it is not the default.
//     * @return the key name
//     */
//    default String naturalKeyName() {
//        return idName();
//    }

}
