package nl.fontys.sebivenlo.dao;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TransactionTokenTest {

    TransactionToken tok = new TransactionToken() {
    };

    /**
     * Test that both default methods have no effect. For coverage.
     * @throws java.lang.Exception not expected
     */
    @Test
    public void testCallinMethodsCausesNoTrouble() throws Exception {
        
        tok.commit();
        tok.rollback();
        
        //fail( "testMethod not yet implemented. Review the code and comment or delete this line" );
    }

}
