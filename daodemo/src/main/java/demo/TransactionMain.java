package demo;

import entities.DBTestHelpers;
import entities.Department;
import entities.DepartmentMapper;
import entities.Employee;
import entities.EmployeeMapper;
import entities.PGDataSource;
import java.sql.SQLException;
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


    public static void main( String[] args ) throws SQLException {
          DBTestHelpers.doDDL( "truncate table departments cascade" );

        // register a mapper for employee
        PGDAOFactory pdaof = new PGDAOFactory( PGDataSource.DATA_SOURCE );
        pdaof.registerMapper(Employee.class, new EmployeeMapper() );
        DepartmentMapper departmentMapper2 = new DepartmentMapper( );
        System.out.println( "departmentMapper2 = " + departmentMapper2 );
        pdaof.registerMapper( Department.class, departmentMapper2  );
        

        // get a dao (no transactions).
        DAO<String, Department> dDao = pdaof.createDao( Department.class );

        Department d1 = new Department( "fin" );
        d1.setDescription( "bean counters" );
        d1.setEmail( "fin@vanderheijden.nl" );

        Department savedDepartment = dDao.save( d1 );

        Collection<Department> allDept = dDao.getAll();
        System.out.println( "allDept = " );
        allDept.stream().forEach( System.out::println );
        // undo

        try (
                DAO<Integer, Employee> e2Dao = pdaof.createDao( Employee.class );
                TransactionToken tok = e2Dao.startTransaction(); ) {
            dDao = pdaof.createDao( Department.class, tok );
            Collection<Employee> all = e2Dao.getAll();
            System.out.println( "all = " + all );
            Employee p = new Employee( 0 );
            p.setFirstname( "Sharon" );
            p.setLastname( "Hendrix" );
            p.setEmail( "s.hendrix@student.fontys.nl" );
            p.setDepartment( "fin" );
            Employee savedSharon = e2Dao.save( p );

            all = e2Dao.getAll();
            System.out.println( "now all = " );
            all.stream().forEach( System.out::println );

            // clean up 
            e2Dao.delete( savedSharon );
            dDao.delete( savedDepartment );
            tok.commit();
        } catch ( Exception ex ) {
            Logger.getLogger( MainDB.class.getName() ).log( Level.SEVERE, null,
                    ex );
        }

        // undo
    }
}
