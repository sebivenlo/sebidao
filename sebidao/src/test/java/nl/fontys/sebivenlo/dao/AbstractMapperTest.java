package nl.fontys.sebivenlo.dao;

import entities.Company;
import entities.Company2;
import entities.CompanyMapper;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
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
        assertThat( mapper.assembler ).isNotNull();
    }

    @Test
    public void testCreateCompany() {
        Object[] a = new Object[]{ "Pieters", "Belgium", "Waterschei",
            "Molenstraat 20", "PITRS", "B46587", 0, null };
        Company comp = mapper.assembler.apply( a );
        assertThat( comp ).as( "Got a product" ).isNotNull();
        System.out.println( "comp = " + comp );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testDisassemblerWithParts() {
        MyMapper m = new MyMapper();

        Company c = new Company( "MARS", "Netherlands", "Veghel", "Spoorstraat", "MARS", "4523AH" );

        assertThat( m.disAssembler ).as( "A dissambler should have been found" )
                .isNotNull();

        Object[] parts = m.explode( c );
        assertThat( parts ).isNotNull();
        assertThat( parts.length ).isEqualTo( m.persistentFieldNames().size() );

//        Assert.fail( "method method reached end. You know what to do." );
    }

    @Test
    public void testDisassemblerReflected() {
        MyMapper2 m2 = new MyMapper2();

        Company2 c = new Company2( "MARS", "Netherlands", "Veghel", "Spoorstraat", "MARS", "4523AH" );

        assertThat( m2.disAssembler ).isNotNull();
        //m2.disAssembler;
        Object[] parts = m2.explode( c );
        assertThat( parts ).isNotNull();
        assertThat( parts.length ).isEqualTo( m2.persistentFieldNames().size() );
        assertThat( parts[ 2 ] ).isEqualTo( "Veghel" );
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
        Object[] parts = new Object[]{ "A", "B", "C", "D", "E", "F", "G", "H" };
        try {
            Company2 comp = m2.assembler.apply( parts );
        } catch ( Throwable t ) {
            System.out
                    .println( "================== look here ======================" );
//            t.printStackTrace();
            Throwable cause = t.getCause();
            assertThat( cause instanceof IllegalArgumentException )
                    .as( "expected " ).isTrue();
            String message = cause.getMessage();
            assertThat( message.contains( "argument type mismatch" ) ).isTrue();
            String tMessage = t.getMessage();
            System.out.println( "tMessage = " + tMessage );
        }
//        Assert.fail( "method wrongParameterException reached end. You know what to do." );
    }

    @Test
    public void testTableNameAnnotation() {

        String tableName = mapper.tableName();
        assertThat( tableName ).as( "proper plural" ).isEqualTo( "companies" );
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
