package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOTest extends DBTestHelpers {

    PGDAO<Integer, Employee> eDao;

    @Before
    public void setUp() {
        eDao = (PGDAO<Integer, Employee>) daof.createDao( Employee.class );
    }

    @Test
    public void testExecuteInt0() {
        int id = eDao.executeIntQuery( "select employeeid from employees where firstname='Batman'" );
        assertEquals( "not available", 0, id );
        //fail( "test method testExceuteInt0 reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testSize() {
        int id = eDao.size();
        assertEquals( "there should be a piet", 1, id );
        //fail( "test method testExceuteInt0 reached its end, you can remove this line when you aggree." );
    }
    
    @Test(expected = DAOException.class)
    public void testGetByColumnKeyValues() {
        eDao.getByColumnValues( "batcar","black" ); // column not in database should cause sql exception
        //fail( "test method testGetByColumnKeyValues reached its end, you can remove this line when you aggree." );
    }
}
