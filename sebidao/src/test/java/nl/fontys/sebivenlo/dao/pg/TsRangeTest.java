package nl.fontys.sebivenlo.dao.pg;

import java.time.Duration;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.time.temporal.TemporalUnit;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TsRangeTest {

//    @Ignore( "Think TDD" )
    @Test
    public void method() {
        
        LocalDateTime now = now();
        LocalDateTime nextHour = now().plus( 1, HOURS );
        LocalDateTime in10Minutes = now().plus( 10, MINUTES );

        TsRange ts0 = new TsRange( now, Duration.of( 20, MINUTES ) );
        TsRange ts1 = new TsRange( nextHour, Duration.of( 20, MINUTES ) );
        assertFalse( "no 1", ts0.overlaps( ts1 ) );
        assertFalse( "no 2", ts1.overlaps( ts0 ) );

        TsRange ts2 = new TsRange( in10Minutes, Duration.of( 20, MINUTES ) );
        assertTrue( "yes 1", ts0.overlaps( ts2 ) );
        assertTrue( "yes 2", ts2.overlaps( ts0 ) );
        
        TsRange ts3 = new TsRange( in10Minutes, Duration.of( 3, HOURS ) );
        assertTrue( "yes 3", ts1.overlaps( ts3 ) );
        assertTrue( "yes 3", ts3.overlaps( ts1 ) );
        

//        Assert.fail( "method method reached end. You know what to do." );
    }

}
