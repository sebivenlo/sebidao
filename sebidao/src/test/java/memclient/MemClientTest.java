
package memclient;

import entities.Employee;
import entities.EmployeeMapper2;
import nl.fontys.sebivenlo.dao.memory.MemoryDAOFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hom
 */
public class MemClientTest {
    nl.fontys.sebivenlo.dao.DAO<Integer,Employee> dao;
    public MemClientTest() {
        MemoryDAOFactory fac = MemoryDAOFactory.getInstance();
        fac.registerMapper( Employee.class, new EmployeeMapper2() );
        dao = fac.createDao( Employee.class);
        
    }
 
//    @Ignore
    @Test
    public void testIBi() {
        Employee ibi = new Employee(42,"Kouzak","Ibrahim","i@there.com",4);
        
        Employee saveDIbi = dao.save( ibi );
        
        assertNotNull(saveDIbi);
        
        assertFalse(saveDIbi.toString().isEmpty());
        System.out.println( "saveDIbi = " + saveDIbi );
        fail( "testIBi reached it's and. You will know what to do." );
    }
}
