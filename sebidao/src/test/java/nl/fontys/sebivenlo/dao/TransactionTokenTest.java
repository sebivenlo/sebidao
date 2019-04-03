package nl.fontys.sebivenlo.dao;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Coverage. 
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TransactionTokenTest {

    TransactionToken tok = new TransactionToken() {
    };

    /**
     * Test that all default methods have no effect. For coverage.
     * @throws java.lang.Exception not expected
     */
    @Test
    public void testCallinMethodsCausesNoTrouble() throws Exception {
        
        tok.commit();
        tok.rollback();
        tok.close();
        //fail( "testMethod not yet implemented. Review the code and comment or delete this line" );
    }
    
    

}
