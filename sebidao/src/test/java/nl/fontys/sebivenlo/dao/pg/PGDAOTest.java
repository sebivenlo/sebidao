package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOTest extends DBTestHelpers {

    PGDAO<Integer, Employee> eDao;

    @BeforeClass
    public static void setupClass() {
        DBTestHelpers.setupClass();
        loadDatabase();
    }

    @Before
    public void setUp() throws Throwable {
        assertNotNull( daof );
        eDao = (PGDAO<Integer, Employee>)daof.createDao( Employee.class );
    }

    @Test
    public void testExecuteInt0() {
        int id = eDao.executeIntQuery(
                "select employeeid from employees where firstname='Batman'" );
        assertEquals( "not available", 0, id );
        //fail( "test method testExceuteInt0 reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testSize() {
        int id = eDao.size();
        assertEquals( "there should be a piet", 1, id );
        //fail( "test method testExceuteInt0 reached its end, you can remove this line when you aggree." );
    }

    @Test( expected = DAOException.class )
    public void testGetByColumnKeyValues() {
        eDao.getByColumnValues( "batcar", "black" ); // column not in database should cause sql exception
        //fail( "test method testGetByColumnKeyValues reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testExecuteIntQueryInt1() {
        int id = eDao.executeIntQuery(
                "select departmentid from employees where employeeid=?", 1 );
        assertEquals( "piet is in dept 1", 1, id );
        // fail( "testExecuteIntQueryInt1 not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetIdForKey() {
        int id = eDao.getIdForKey( 1 );
        assertEquals( "piet has id 1", 1, id );

        //fail( "testGetIdForKey not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetConnection() {
        Connection connection = eDao.getConnection();
        assertNotNull( "having my connection", connection );
        // fail( "testGetConnection not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testUsingTransactionConnection() throws SQLException {
        PGTransactionToken tok = new PGTransactionToken( ds.getConnection() );
        Connection tokCon = tok.getConnection();
        eDao.setTransactionToken( tok );
        Connection c = eDao.getConnection();
        assertSame( "using one and the same connection", tokCon, c );
        assertSame( tok, eDao.getTransactionToken() );

        //fail( "testUsingTransactionConnection not yet implemented. Review the code and comment or delete this line" );
    }
}
