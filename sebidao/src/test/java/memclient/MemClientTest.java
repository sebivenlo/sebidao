package memclient;

import entities.Employee;
import entities.EmployeeMapper2;
import nl.fontys.sebivenlo.dao.memory.InMemoryDAO;
import nl.fontys.sebivenlo.dao.memory.MemoryDAOFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Note that entities saved in the dao are automagically persisted to disk on (test) JVM shutdown.
 * 
 * @author hom
 */
public class MemClientTest {

    nl.fontys.sebivenlo.dao.DAO<Integer, Employee> dao;
    MemoryDAOFactory fac = MemoryDAOFactory.getInstance();

    public MemClientTest() {
        fac.registerMapper( Employee.class, new EmployeeMapper2() );
        dao = fac.createDao( Employee.class );

    }

//    @Ignore
    @Test
    public void testIBi() {
        Employee ibi = new Employee( 42, "Kouzak", "Ibrahim", "i@there.com", 4 );

        Employee saveDIbi = dao.save( ibi );

        assertNotNull( saveDIbi );

        assertFalse( saveDIbi.toString().isEmpty() );
        System.out.println( "saveDIbi = " + saveDIbi );
//        fail( "testIBi reached it's and. You will know what to do." );
    }
    
}
