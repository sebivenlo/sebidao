package nl.fontys.sebivenlo.dao;

import entities.Department;
import entities.Employee;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
public class EntityMetaDataTest {

    @Test
    public void testMappedNames() {

        String[] names = { "employeeid", "lastname", "firstname", "email", "departmentid" };

        EntityMetaData<Employee> emd = new EntityMetaData<>( Employee.class );

        assertThat( emd.typeMap.keySet() ).containsExactlyInAnyOrder( names );

        //Assert.fail( "test method testMappedNames reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testGetIDAnnotation() {
        EntityMetaData<Employee> emd = new EntityMetaData<>( Employee.class );

        assertEquals( "employeeid", emd.getIdName() );
        // Assert.fail( "test method testGetIDAnnotation reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testGetIDType() {
        EntityMetaData<Employee> emd = new EntityMetaData<>( Employee.class );

        assertEquals( "the proper id type is Integer", Integer.class, emd.getIdType() );
        // Assert.fail( "test method testGetIDAnnotation reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testIdGeneratedTrue() {
        EntityMetaData<Employee> emd = new EntityMetaData<>( Employee.class );
        assertTrue( emd.isIDGenerated() );

        //Assert.fail( "test method testIdGenerated reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testIdGeneratedFalse() {
        EntityMetaData<Department> emd = new EntityMetaData<>( Department.class );
        assertFalse( emd.isIDGenerated() );

        //Assert.fail( "test method testIdGenerated reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testGeneratedFieldFalse() {
        EntityMetaData<Department> emd = new EntityMetaData<>( Department.class );
        assertFalse( emd.isGenerated( "name" ) );

        //Assert.fail( "test method testIdGenerated reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testGeneratedFieldTrue() {
        EntityMetaData<Department> emd = new EntityMetaData<>( Department.class );
        assertTrue( emd.isGenerated( "departmentid" ) );

        //Assert.fail( "test method testIdGenerated reached its end, you can remove this line when you aggree." );
    }
}
