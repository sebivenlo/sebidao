package nl.fontys.sebivenlo.dao.pg;

import entities.Company;
import entities.CompanyMapper;
import entities.DBTestHelpers;
import entities.Department;
import entities.DepartmentMapper;
import entities.Employee;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
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
        eDao = daof.createDao( Employee.class );
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

    @Test
    public void testSaveAll() throws SQLException, Exception {
        LocalDate d = LocalDate.of( 1999, 5, 6 );
        Employee jan = new Employee( 0, "Klaassen", "Jan", "j.klaassen@fontys.nl", 1, true, d );
        Employee kat = new Employee( 0, "Hansen", "Katrien", "j.hansen@fontys.nl", 1, false, d );
        DAO dao = daof.createDao( Employee.class );
        try (
                TransactionToken tok = dao.startTransaction(); ) {
            Collection<Employee> saveAll = dao.saveAll( jan, kat );
            assertEquals( "should have 2 saved emps", 2, saveAll.size() );
            //Assertions.assertThat( saveAll).containsExactlyInAnyOrder( kat, jan );
            Employee e1 = saveAll.iterator().next();
            Employee e2 = saveAll.iterator().next();
            dao.deleteAll( e1, e2 );
            tok.commit();
        }

        //Assert.fail( "test method testSaveAll reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testNullableFields() {
        String tick = "BAS";
        Company c = new Company( null, null, null, null, tick, null );
        daof.registerMapper( Company.class, new CompanyMapper() );
        DAO<String, Company> cdao = daof.createDao( Company.class );

        Company sC = cdao.save( c );

        assertEquals( tick, sC.getTicker() );

        assertNull( "name is null", sC.getName() );
        assertEquals( "someint", 0, sC.getSomeInt() );
        assertEquals( "someinteger", null, sC.getSomeInteger() );

        //Assert.fail( "test method testNullableFields reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testDropGenerated() {
        DepartmentMapper departmentMapper = new DepartmentMapper();
        daof.registerMapper( Department.class, departmentMapper );

        PGDAO<String, Department> ddao = daof.createDao( Department.class );
        Department e = new Department( "engineering", "The geniusses", "engineering@example.com,", null );

        Object[] parts = departmentMapper.explode( e );
        assertEquals( "part count", 4, parts.length );

        Object[] nonGeneratedParts = ddao.dropGeneneratedParts( parts );
        assertEquals( "normal field count", 3, nonGeneratedParts.length );

//        Assert.fail( "test testDropGenerated reached its end, you can remove me when you aggree." );
    }
    
}
