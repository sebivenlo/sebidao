package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import entities.EmployeeMapper2;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.TransactionToken;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public abstract class PGDAOExceptionTestBase extends DBTestHelpers {

    Employee gp = new Employee( 1 );
    @Mock
    DataSource ds;
    
    DAO<Integer, Employee> eDao;
    EmployeeMapper2 mapper;
    private Connection connection;

    abstract Connection getConnection();
    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );
        this.connection = getConnection();
        Mockito.when( ds.getConnection() ).thenReturn( this.connection );
        mapper = new EmployeeMapper2( Integer.class, Employee.class );
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, mapper );
        eDao = daof.createDao( Employee.class );
    }
    

    @After
    public void cleanup() {
        try {
            this.connection.close();
        } catch ( SQLException zipit ) {
            Logger.getLogger( PGDAOExceptionTestBase.class.getName() ).
                    log( Level.SEVERE, null, zipit );
        }
    }

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
        ((PGDAO)eDao).executeIntQuery( "select count(1) from employees" );
        fail( "test method testIntQuery reached its end, you can remove this line when you aggree." );
    }

    @Test( expected = DAOException.class )
    public void testLastId() {
        eDao.lastId();
    }

    @Test( expected = DAOException.class )
    public void testSave() throws Exception {
        TransactionToken tok = eDao.getTransactionToken();
        if ( null != tok ) {
            tok.close();
        }
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

    @Test( expected = DAOException.class )
    public void testExecuteIntQueryInt1() {
        ((PGDAO)eDao).executeIntQuery(
                "select departmentid from employees where employeeid=?", 1 );
        // fail( "testExecuteIntQueryInt1 not yet implemented. Review the code and comment or delete this line" );
    }

}
