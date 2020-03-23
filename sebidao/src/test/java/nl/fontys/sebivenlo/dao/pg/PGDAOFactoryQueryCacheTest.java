/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOFactoryQueryCacheTest extends PGDAOTestBase {

    @Test
    public void testCreatesCache() {
        PGDAO<Integer, Employee> edao = daof.createDao( Employee.class );
        int size = daof.queryStringCache.size();
        assertThat( size ).as( "has one elements" ).isEqualTo( 1 );
    }

    @Test
    public void testComputeSelect() {
        PGDAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Optional<Employee> get = edao.get( 1 );
        int size = edao.queryTextCache.size();
        assertThat( 1 <= size ).as( "the proper id type is Integer" ).isTrue();
        //Assert.fail( "test method testComputeSelect reached its end, you can remove this line when you aggree." );
    }

}
