/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao.pg;

import entities.Employee;
import entities.EmployeeMapper2;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test if all sql exceptions are properly translated (as in wrapped in) into
 * DAOExceptions. For coverage. The methods all will find a connection that is
 * broken and throws an exception.
 *
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOExceptionTest {

    Connection conn = mock( Connection.class, anything -> {
        throw new SQLException( "I am supposed to" );
    } );

    @Mock
    DataSource ds;

    AbstractDAOFactory daof;
    EmployeeMapper2 mapper;
    PGDAO<Integer, Employee> eDao;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );
        Mockito.when( ds.getConnection() ).thenReturn( conn );
        mapper = new EmployeeMapper2( Integer.class, Employee.class );
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, mapper );
        eDao = (PGDAO<Integer, Employee>)daof.createDao( Employee.class );
    }

    Employee gp = new Employee( 1 );

    @Test( expected = DAOException.class )
    public void testGet() {
        eDao.get( 1 );
    }

    @Test( expected = DAOException.class )
    public void testDelete() {
        eDao.delete( gp );
    }

    @Test( expected = DAOException.class )
    public void testUpdate() {
        eDao.update( gp );
    }

    @Test( expected = DAOException.class )
    public void testSave() {
        eDao.save( gp );
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
    public void testLastId() {
        eDao.lastId();
    }

    @Test( expected = DAOException.class )
    public void testSize() {
        eDao.size();
    }

    @Test
    public void testSetTransactionToken() {
        eDao.setTransactionToken( null );
        assertTrue( "should run without issues", true );
    }


    @Test( expected = DAOException.class )
    public void testIntQuery() {
        eDao.executeIntQuery( "select count(1) from employee" );
        
        fail( "test method testIntQuery reached its end, you can remove this line when you aggree." );
    }
    
    @Test( expected = DAOException.class )
    public void testStartTransaction() throws Exception {
        eDao.startTransaction();
    }
}
