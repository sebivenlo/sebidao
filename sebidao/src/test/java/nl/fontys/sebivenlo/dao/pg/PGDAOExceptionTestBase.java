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
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
abstract class PGDAOExceptionTestBase extends DBTestHelpers {

    Employee gp = new Employee( 1 );
    @Mock
    DataSource ds;

    DAO<Integer, Employee> eDao;
    EmployeeMapper2 mapper;
    private Connection connection;

    abstract Connection getConnection();

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );
        this.connection = getConnection();
        Mockito.when( ds.getConnection() ).thenReturn( this.connection );
        mapper = new EmployeeMapper2();
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, mapper );
        eDao = daof.createDao( Employee.class );
    }

    @After
    void cleanup() {
        try {
            this.connection.close();
        } catch ( SQLException zipit ) {
            Logger.getLogger( PGDAOExceptionTestBase.class.getName() ).
                    log( Level.SEVERE, null, zipit );
        }
    }

    @Test
    void delete() {
        ThrowingCallable suspectCode = () -> {
            eDao.delete( gp );
        };
        assertThatThrownBy( suspectCode ).isInstanceOf( DAOException.class );
    }

    @Test
    void get() throws SQLException {
        assertThatThrownBy( () -> {
            eDao.get( 1 );
        } ).isInstanceOf( DAOException.class );

    }

    @Test
    void getAll() {
        assertThatThrownBy( () -> {
            eDao.getAll();
        } ).isInstanceOf( DAOException.class );

    }

    @Test
    void getByColumnValues() {
        assertThatThrownBy( () -> {
            eDao.getByColumnValues( "name", "nothing" );
        } ).isInstanceOf( DAOException.class );

    }

    @Test
    void intQuery() {
        assertThatThrownBy( () -> {
            ( (PGDAO) eDao ).executeIntQuery( "select count(1) from employees" );
        } ).isInstanceOf( DAOException.class );
//        fail( "test method testIntQuery reached its end, you can remove this line when you aggree." );
    }

    @Test
    void lastId() {
        assertThatThrownBy( () -> {
            eDao.lastId();
        } ).isInstanceOf( DAOException.class );
    }

    @Test
    void save() throws Exception {
        assertThatThrownBy( () -> {
            TransactionToken tok = eDao.getTransactionToken();
            if ( null != tok ) {
                tok.close();
            }
            eDao.save( gp );
        } ).isInstanceOf( DAOException.class );
    }

    @Test
    void setTransactionToken() {
        assertThatCode( () -> {
            eDao.setTransactionToken( null );
        } ).doesNotThrowAnyException();
    }

    @Test
    void size() {
        assertThatThrownBy( () -> {
            eDao.size();
        } ).isInstanceOf( DAOException.class );
    }

    @Test
    void update() {
        assertThatThrownBy( () -> {
            eDao.update( gp );
        } ).isInstanceOf( DAOException.class );
    }

    @Test
    void executeIntQueryInt1() {
        assertThatThrownBy( () -> {
            ( (PGDAO) eDao ).executeIntQuery(
                    "select departmentid from employees where employeeid=?", 1 );
        } ).isInstanceOf( DAOException.class );

        // fail( "testExecuteIntQueryInt1 not yet implemented. Review the code and comment or delete this line" );
    }

}
