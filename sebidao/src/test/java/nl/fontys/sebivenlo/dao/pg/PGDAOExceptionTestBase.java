package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import entities.EmployeeMapper2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAOException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public abstract class PGDAOExceptionTestBase extends DBTestHelpers {

    Employee gp = new Employee( 1 );
    AbstractDAOFactory daof;
    @Mock
    DataSource ds;
    PGDAO<Integer, Employee> eDao;
    EmployeeMapper2 mapper;
    
    abstract Connection getConnection();

    @Test( expected = DAOException.class )
    public void testDelete() {
        eDao.delete( gp );
    }

    @Test( expected = DAOException.class )
    public void testGet() throws SQLException {
        eDao.get( 1 );
    }

    @Test( expected = DAOException.class )
    public void testGetAll() {
        eDao.getAll();
    }

    @Test( expected = DAOException.class )
    public void testGetByColumnValues() {
        eDao.getByColumnValues( "name", "nothing" );
    }

    @Test( expected = DAOException.class )
    public void testIntQuery() {
        eDao.executeIntQuery( "select count(1) from employee" );
        fail( "test method testIntQuery reached its end, you can remove this line when you aggree." );
    }

    @Test( expected = DAOException.class )
    public void testLastId() {
        eDao.lastId();
    }

    @Test( expected = DAOException.class )
    public void testSave() {
        eDao.save( gp );
    }

    @Test
    public void testSetTransactionToken() {
        eDao.setTransactionToken( null );
        assertTrue( "should run without issues", true );
    }

    @Test( expected = DAOException.class )
    public void testSize() {
        eDao.size();
    }

    @Test( expected = DAOException.class )
    public void testUpdate() {
        eDao.update( gp );
    }

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );
        Mockito.when( ds.getConnection() ).thenReturn( getConnection() );
        mapper = new EmployeeMapper2( Integer.class, Employee.class );
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, mapper );
        eDao = (PGDAO<Integer, Employee>) daof.createDao( Employee.class );
    }
}
