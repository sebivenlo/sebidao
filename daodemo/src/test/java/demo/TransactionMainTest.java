package demo;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TransactionMainTest {

//    @Disabled
    @Test
    public void testMain() {
        try{
        TransactionMain.main( new String[0]);
        } catch(Throwable t){
            Logger.getLogger( "main").log( Level.INFO, t.getMessage() );
        }
    }

}
