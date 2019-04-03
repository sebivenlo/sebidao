package demo;

import entities.DepartmentMapper;
import entities.Department;
import entities.Employee;
import entities.EmployeeMapper;
import entities.PGDataSource;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TransactionMain {

    public static String EMPLOYEE_FILE = "employee.ser";

    public static void main( String[] args ) {

        // register a mapper for employee
        PGDAOFactory pdaof = new PGDAOFactory( PGDataSource.DATA_SOURCE );
        pdaof.registerMapper( Employee.class, new EmployeeMapper() );
        pdaof.registerMapper( Department.class, new DepartmentMapper() );

        // get a dao (no transactions).
        //DAO<Integer, Employee> eDao = pdaof.createDao( Employee.class );
        DAO<String, Department> dDao = pdaof.createDao( Department.class );

        Department d1 = new Department( "fin" );
        d1.setDescription( "bean counters" );
        d1.setEmail( "fin@vanderheijden.nl" );

        Department saveDept = dDao.save( d1 );

        Collection<Department> allDept = dDao.getAll();
        System.out.println( "allDept = " );
        allDept.stream().forEach( System.out::println );

        
        try ( 
                
        DAO<Integer,Employee> e2Dao = pdaof.createDao( Employee.class );
                TransactionToken tok = eDao.startTransaction(); ) {
            dDao = pdaof.createDao( Department.class, tok);
            Collection<Employee> all = e2Dao.getAll();
            System.out.println( "all = " + all );
            Employee p = new Employee( 0 );
            p.setFirstname( "Sharon" );
            p.setLastname( "Hendrix" );
            p.setEmail( "s.hendrix@student.fontys.nl" );
            p.setDepartment( "fin" );
            Employee sharonInDb = eDao.save( p );

            all = e2Dao.getAll();
            System.out.println( "now all = " );
            all.stream().forEach( System.out::println );

            // clean up 
            e2Dao.delete( sharonInDb );
            dDao.delete( saveDept );
            tok.commit();
        } catch ( Exception ex ) {
            Logger.getLogger( MainDB.class.getName() ).log( Level.SEVERE, null,
                    ex );
        }
    }
}
