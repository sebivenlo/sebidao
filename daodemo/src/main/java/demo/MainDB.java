package demo;

import entities.DepartmentMapper;
import entities.Department;
import entities.Employee;
import entities.EmployeeMapper;
import entities.PGDataSource;
import java.util.Collection;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.memory.InMemoryDAO;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class MainDB {

    public static String EMPLOYEE_FILE = "employee.ser";

    public static void main( String[] args ) {

        // register a mapper for employee
        PGDAOFactory pdaof = new PGDAOFactory( PGDataSource.DATA_SOURCE );
        pdaof.registerMapper( Employee.class, new EmployeeMapper() );
        pdaof.registerMapper( Department.class, new DepartmentMapper() );

        // get a dao (no transactions.
        DAO<Integer, Employee> eDao = pdaof.createDao( Employee.class );
        DAO<Integer, Department> dDao = pdaof.createDao( Department.class );

        Department d1 = new Department( 2 );
        d1.setEmail( "sales@vanderheijden.nl" );

        d1.setDescription( "sales" );
        d1.setName( "sales" );
        Department saveDept = dDao.save( d1 );
        
        Collection<Department> allDept = dDao.getAll();
        allDept.stream().forEach( System.out::println);
        System.out.println( "allDept = " + allDept );
        dDao.delete( saveDept);
        
        Collection<Employee> all = eDao.getAll();
        System.out.println( "all = " + all );
        Employee p = new Employee( 0 );
        p.setFirstname( "Sharon" );
        p.setLastname( "Hendrix" );
        p.setEmail( "s.hendrix@student.fontys.nl" );
        p.setDepartmentid( 1 );
        Employee sharonInDb = eDao.save( p );

        all = eDao.getAll();
        System.out.println( "now all = " + all );
        eDao.delete( sharonInDb );
    }
}
