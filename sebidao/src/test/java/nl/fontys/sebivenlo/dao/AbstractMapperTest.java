package nl.fontys.sebivenlo.dao;

import entities.Company;
import entities.Company2;
import entities.CompanyMapper;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class AbstractMapperTest {

    AbstractMapper<String, Company> mapper;

    @Before
    public void setup() {

        mapper = new CompanyMapper();

    }

    @Test
    public void testCreateMapper() {
        assertNotNull( "got assembler", mapper.assembler );
    }

    @Test
    public void testCreateCompany() {
        Object[] a = new Object[] { "Pieters", "Belgium", "Waterschei",
            "Molenstraat 20", "PITRS", "B46587", 0, null };
        Company comp = mapper.assembler.apply( a );
        assertNotNull( "Got a product", comp );
        System.out.println( "comp = " + comp );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testDisassemblerWithParts() {
        MyMapper m = new MyMapper();

        Company c = new Company( "MARS", "Netherlands", "Veghel", "Spoorstraat", "MARS", "4523AH" );

        assertNotNull( "A dissambler should have been found", m.disAssembler );

        Object[] parts = m.explode( c );
        assertNotNull( "No real explosion", parts );
        assertEquals( m.persistentFieldNames().size(), parts.length );

//        Assert.fail( "method method reached end. You know what to do." );
    }

    @Test
    public void testDisassemblerReflected() {
        MyMapper2 m2 = new MyMapper2();

        Company2 c = new Company2( "MARS", "Netherlands", "Veghel", "Spoorstraat", "MARS", "4523AH" );

        assertNotNull( "A dissambler should have been found", m2.disAssembler );
        //m2.disAssembler;
        Object[] parts = m2.explode( c );
        assertNotNull( "No real explosiion", parts );
        assertEquals( m2.persistentFieldNames().size(), parts.length );
        assertEquals( "Veghel", parts[ 2 ] );
//        Assert.fail( "method method reached end. You know what to do." );
    }

    /**
     * Test if the exception message contains sufficient hints to have the user
     * supply the proper array content.
     */
    //@Ignore( "Think TDD" )
    @Test
    public void wrongParameterException() {

        // 8 parts, all strings should lead to exception
        MyMapper2 m2 = new MyMapper2();
        Object[] parts = new Object[] { "A", "B", "C", "D", "E", "F", "G", "H" };
        try {
            Company2 comp = m2.assembler.apply( parts );
        } catch ( Throwable t ) {
            System.out.println( "================== look here ======================" );
//            t.printStackTrace();
            Throwable cause = t.getCause();
            assertTrue("expected ",cause instanceof IllegalArgumentException);
            String message = cause.getMessage();
            assertTrue(message.contains( "argument type mismatch"));
            String tMessage = t.getMessage();
            System.out.println( "tMessage = " + tMessage );
        }
//        Assert.fail( "method wrongParameterException reached end. You know what to do." );
    }

    
    @Test
    public void testTableNameAnnotation() {
        
        String tableName = mapper.tableName();
        assertEquals("proper plural", "companies",tableName);
    }
    
    static class MyMapper extends AbstractMapper<String, Company> {

        public MyMapper() {
            super( String.class, Company.class );
        }

        @Override
        public Function<Company, String> keyExtractor() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }
    }

    static class MyMapper2 extends AbstractMapper< String, Company2> {

        public MyMapper2() {
            super( String.class, Company2.class );
        }

        @Override
        public Function<Company2, String> keyExtractor() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
