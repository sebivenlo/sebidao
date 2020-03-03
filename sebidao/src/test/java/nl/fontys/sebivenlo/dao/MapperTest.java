package nl.fontys.sebivenlo.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class MapperTest {
    
    static AbstractMapper<String,String> mapper = new AbstractMapper(Integer.class,Student.class){
        @Override
        public Object[] explode( Object e ) {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Function keyExtractor() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
    @Test
    public void testTableName() {
        String simpleName = mapper.tableName();
        assertThat(simpleName).as("expect simple plural of entity type").isEqualTo("students");
        //fail( "testTableName not yet implemented. Review the code and comment or delete this line" );
    }

    
    @Test
    public void testNaturalKeyName() {
        String naturalKeyName = mapper.naturalKeyName();
        assertThat(naturalKeyName).as("naturla key name").isEqualTo("studentid");
        //fail( "test method testNaturalKeyName reached its end, you can remove this line when you aggree." );
    }
    
    private static class Student {

      
    }
}
