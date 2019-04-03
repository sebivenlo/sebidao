package nl.fontys.sebivenlo.dao;

import java.util.Set;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class MapperTest {
    
    static Mapper<String,String> mapper = new Mapper(){
        @Override
        public Class entityType() {
            return Student.class;
        }

        @Override
        public Object[] explode( Object e ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Object implode( Object[] parts ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Set<String> keyNames() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Function keyExtractor() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Set<String> persistentFieldNames() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }
    };
    
    @Test
    public void testTableName() {
        String simpleName = mapper.tableName();
        assertEquals("expect simple plural of entity type","students",simpleName);
        //fail( "testTableName not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testGeneratedKeyIsDefault() {
        assertTrue(mapper.generateKey());
        //fail( "testGeneratedKeyIsDefault not yet implemented. Review the code and comment or delete this line" );
    }
    
    @Test
    public void testNaturalKeyName() {
        String naturalKeyName = mapper.naturalKeyName();
        assertEquals("naturla key name","studentid", naturalKeyName);
        //fail( "test method testNaturalKeyName reached its end, you can remove this line when you aggree." );
    }
    
    private static class Student {

      
    }
}
