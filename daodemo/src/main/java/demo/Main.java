package demo;

import entities.Department;
import entities.Employee;
import entities.EmployeeMapper;
import java.util.Collection;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.memory.MemoryDAOFactory;
import nl.fontys.sebivenlo.dao.memory.InMemoryDAO;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class Main {

    public static String EMPLOYEE_FILE = "employee.ser";

    public static void main( String[] args ) {
        InMemoryDAO<Integer,Employee> eDao = new InMemoryDAO<>( Employee.class );
        MemoryDAOFactory.getInstance().registerMapper( Employee.class,new EmployeeMapper());

//        Department d1 = new Department( 1 );
//
//        d1.setDescription( "sales" );
//        d1.setName( "sales" );
//        //dDao.save( d1 );
        Collection<Employee> all = eDao.getAll();
        System.out.println( "all = " + all );
//        Employee p = new Employee( 1 );
//        p.setFirstname( "Piet" );
//        p.setLastname( "Puk" );
//        p.setDepartmentid( 1 );
//
//        eDao.save( p );

    }
}
