package demo;

import entities.Department;
import entities.Employee;
import nl.fontys.sebivenlo.dao.DaoFactory;
import nl.fontys.sebivenlo.dao.memory.InMemoryDAO;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class MainDB {

    public static String EMPLOYEE_FILE = "employee.ser";

    public static void main( String[] args ) {
        InMemoryDAO<Integer,Employee> eDao = new InMemoryDAO<>( Employee.class );
        DaoFactory.INSTANCE.put( Employee.class, eDao );

       // InMemoryDAO<Integer,Department> dDao = new InMemoryDAO<>( Department.class );

        DaoFactory.INSTANCE.put( Employee.class, eDao );

        Department d1 = new Department( 1 );

        d1.setDescription( "sales" );
        d1.setName( "sales" );
       // dDao.save( d1 );

        Employee p = new Employee( 1 );
        p.setFirstname( "Piet" );
        p.setLastname( "Puk" );
        p.setDepartmentid( 1 );

        eDao.save( p );

    }
}
