package demo;

import entities.DBTestHelpers;
import entities.DepartmentMapper;
import entities.Department;
import entities.Email;
import entities.Employee;
import entities.EmployeeMapper;
import entities.PGDataSource;
import java.sql.SQLException;
import java.util.Collection;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class MainDB {

    public static String EMPLOYEE_FILE = "employee.ser";

    public static void main( String[] args ) throws SQLException {
          DBTestHelpers.doDDL( "truncate table departments cascade" );
        // register a mapper for employee
        PGDAOFactory pdaof = new PGDAOFactory( PGDataSource.DATA_SOURCE );
        pdaof.registerPGMashallers( Email.class, Email::new, x -> PGDAOFactory.pgobject( "citext", x ) );
        pdaof.registerMapper( Employee.class, new EmployeeMapper() );
        pdaof.registerMapper( Department.class, new DepartmentMapper() );

        
        
        // get a dao (no transactions.
        DAO<Integer, Employee> eDao = pdaof.createDao( Employee.class );
        DAO<String, Department> dDao = pdaof.createDao( Department.class );

        Department d1 = new Department( "fin" );
        d1.setDescription( "bean counters" );
        d1.setEmail( "fin@vanderheijden.nl" );

        Department savedDept = dDao.save( d1 );

        Collection<Department> allDept = dDao.getAll();
        System.out.println( "allDept = " );
        allDept.stream().forEach( System.out::println );

        Collection<Employee> all = eDao.getAll();
        System.out.println( "all = " + all );
        Employee p = new Employee( 0 );
        p.setFirstname( "Sharon" );
        p.setLastname( "Hendrix" );
        p.setEmail( "s.hendrix@student.fontys.nl" );
        p.setDepartment( "fin" );
        Employee sharonInDb = eDao.save( p );

        all = eDao.getAll();
        System.out.println( "now all = " );
        all.stream().forEach( System.out::println );

        // clean up 
        eDao.delete( sharonInDb );
        dDao.delete( savedDept );
    }
    
}
