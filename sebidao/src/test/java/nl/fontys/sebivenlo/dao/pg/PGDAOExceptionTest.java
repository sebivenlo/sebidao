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
import org.mockito.MockitoAnnotations;

/**
 * For coverage. The methods all will find a connection that is broken and
 * throws an exception.
 *
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOTest {

    @Mock
    Connection conn;

    @Mock
    DataSource ds;

    AbstractDAOFactory daof;
    EmployeeMapper2 mapper;
    DAO<Integer, Employee> eDao;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );
        Mockito.doThrow( new SQLException( "Just for fun" ) ).when( conn ).setAutoCommit( false );
        Mockito.when( ds.getConnection() ).thenReturn( conn );
        mapper = new EmployeeMapper2( Integer.class, Employee.class );
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, mapper );
        eDao = daof.createDao( Employee.class );
    }

    @Test( expected = DAOException.class )
    public void testGet() {
        assertNotNull( eDao );
        Optional<Employee> numberone = eDao.get( 1 );
    }

    @Test
    public void testDelete() {
        fail( "testDelete" );
    }

    @Test
    public void testUpdate() {
        fail( "testUpdate" );
    }

    @Test
    public void testSave() {
        fail( "testSave" );
    }

    @Test
    public void testGetAll() {
        fail( "testGetAll" );
    }

    @Test
    public void testGetByColumnValues() {
        fail( "testGetByColumnNames" );
    }

    @Test
    public void testLastId() {
        fail( "testGetLastId" );
    }

    @Test
    public void testExecuteIntQuery_String() {
        fail( "testExecuteIntQuery" );
    }

    @Test
    public void testExecuteIntQuery_String_GenericType() {
        fail( "testExecuteIntQuery_String_GenericType" );
    }

    @Test
    public void testSize() {
        fail( "testSize" );
    }

    @Test
    public void testGetTransactionToken() {
        fail( "testGetTransactionToken" );
    }

    @Test
    public void testSetTransactionToken() {
        fail( "testSize" );
    }

    @Test
    public void testStartTransaction() throws Exception {
        fail( "testStartTransaction" );
    }

    @Test
    public void testGetIdForKey() {
        fail( "testGetIdForKey" );
    }

}
