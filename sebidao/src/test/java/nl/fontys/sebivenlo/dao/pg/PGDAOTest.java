package nl.fontys.sebivenlo.dao.pg;

import entities.Company;
import entities.CompanyMapper;
import entities.DBTestHelpers;
import entities.Department;
import entities.DepartmentMapper;
import entities.Employee;
import entities.EmployeeMapper2;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.BeforeClass;
import org.junit.Ignore;

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
        assertThat( daof ).isNotNull();
        eDao = daof.createDao( Employee.class );
    }

    @Test
    public void testExecuteInt0() {
        int id = eDao.executeIntQuery(
                "select employeeid from employees where firstname='Batman'" );
        assertThat( id ).isEqualTo( 0 );
        //fail( "test method testExceuteInt0 reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testSize() {
        int id = eDao.size();
        assertThat( id ).as( "there should be a piet" ).isEqualTo( 1 );
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
        assertThat( id ).as( "piet is in dept 1" ).isEqualTo( 1 );
        // fail( "testExecuteIntQueryInt1 not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetIdForKey() {
        int id = eDao.getIdForKey( 1 );
        assertThat( id ).as( "piet has id 1" ).isEqualTo( 1 );

        //fail( "testGetIdForKey not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGetConnection() {
        Connection connection = eDao.getConnection();
        assertThat( connection ).as( "having my connection" ).isNotNull();
        // fail( "testGetConnection not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testUsingTransactionConnection() throws SQLException {
        PGTransactionToken tok = new PGTransactionToken( ds.getConnection() );
        Connection tokCon = tok.getConnection();
        eDao.setTransactionToken( tok );
        Connection c = eDao.getConnection();
        assertThat( c ).as( "using one and the same connection" )
                .isSameAs( tokCon );
        assertThat( eDao.getTransactionToken() ).isSameAs( tok );

        //fail( "testUsingTransactionConnection not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testSaveAll() throws SQLException, Exception {
        LocalDate d = LocalDate.of( 1999, 5, 6 );
        Employee jan = new Employee( 0, "Klaassen", "Jan", "j.klaassen@fontys.nl", 1, true, d );
        Employee kat = new Employee( 0, "Hansen", "Katrien", "j.hansen@fontys.nl", 1, false, d );
        System.out.println( "jan = " + jan );
        System.out.println( "kat = " + kat );
        DAO dao = daof.createDao( Employee.class );
        try (
                 TransactionToken tok = dao.startTransaction(); ) {
            Collection<Employee> saveAll = dao.saveAll( jan, kat );
            assertThat( saveAll.size() )
                    .as( "using one and the same connection" )
                    .isEqualTo( 2 );
            // Assertions.assertThat( saveAll ).containsExactlyInAnyOrder( kat, jan );
            PGDAO pdao = (PGDAO) dao;
            assertThat( pdao.getConnection().isClosed() ).isFalse();
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

        assertThat( sC.getTicker() ).isEqualTo( tick );

        assertThat( sC.getName() ).as( "name is null" ).isNull();
        assertThat( sC.getSomeInt() ).as( "someint" ).isEqualTo( 0 );
        assertThat( sC.getSomeInteger() ).as( "someinteger" ).isEqualTo( null );

        //Assert.fail( "test method testNullableFields reached its end, you can remove this line when you aggree." );
    }

    @Test
    public void testDropGeneratedDept() {
        DepartmentMapper departmentMapper = new DepartmentMapper();
        daof.registerMapper( Department.class, departmentMapper );

        PGDAO<String, Department> ddao = daof.createDao( Department.class );
        Department e = new Department( "engineering", "The geniusses", "engineering@example.com,", null );

        Object[] parts = departmentMapper.explode( e );
        assertThat( parts.length ).as( "part count" ).isEqualTo( 4 );

        Object[] nonGeneratedParts = ddao.dropGeneneratedParts( parts );
        assertThat( nonGeneratedParts.length ).as( "normal field count" )
                .isEqualTo( 3 );

//        Assert.fail( "test testDropGeneratedDept reached its end, you can remove me when you aggree." );
    }

    @Test
    public void testDropGeneratedEmp() {
        EmployeeMapper2 mapper = new EmployeeMapper2();
        LocalDate d = LocalDate.of( 1999, 5, 6 );
        daof.registerMapper( Employee.class, mapper );

        PGDAO<Integer, Employee> edao = daof.createDao( Employee.class );
        Employee jan = new Employee( 0, "Klaassen", "Jan", "j.klaassen@fontys.nl", 1, true, d );

        Object[] parts = mapper.explode( jan );
        assertThat( parts.length ).as( "part count" ).isEqualTo( 7 );

        Object[] nonGeneratedParts = edao.dropGeneneratedParts( parts );
        assertThat( nonGeneratedParts.length ).as( "part count" )
                .isEqualTo( 6 );

        Object o5 = nonGeneratedParts[ 5 ];
        System.out
                .println( "5 field =" + o5.getClass().getSimpleName() + ":" + o5
                        .toString() );
        assertThat( o5 instanceof LocalDate ).as( "last field is dob" ).isTrue();

//        Assert.fail( "test testDropGeneratedDept reached its end, you can remove me when you aggree." );
    }

    @Test
    public void anyQuery() {
        EmployeeMapper2 mapper = new EmployeeMapper2();
        PGDAO<Integer, Employee> eDaoa = daof.createDao( Employee.class );
        String sql = "select * from employees where employeeid = ?";
        List<Employee> list = eDaoa.anyQuery( sql, 1 );
        for ( Employee employee : list ) {
            System.out.println( "employee = " + employee );
        }
        Assert.assertEquals("size", 1, list.size());
        //fail( "method method reached end. You know what to do." );
    }
}
