/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import entities.EmployeeMapper2;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOFactoryQueryCacheTest extends DBTestHelpers {

    @Test
    public void testCreatesCache() {
        PGDAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int size = daof.queryStringCache.size();
        assertEquals( "has one elements", 1, size );
    }

    @Test
    public void testComputeSelect() {
        PGDAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Optional<Employee> get = edao.get( 1 );
        int size = edao.queryTextCache.size();
        assertEquals( "must have a mapping", 1, size );
//        Assert.fail( "test method testComputeSelect reached its end, you can remove this line when you aggree." );
    }
    
}
