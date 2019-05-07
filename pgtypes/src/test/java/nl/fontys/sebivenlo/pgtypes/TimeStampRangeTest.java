package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.*;
import static nl.fontys.sebivenlo.pgtypes.TimeStampRange.fromUntil;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TimeStampRangeTest extends TimeStampTestBase {

    //@Ignore( "Think TDD" )
    @Test
    public void testParse() throws SQLException {
        TimeStampRange ts = new TimeStampRange();
        String value = String.format( "\"[%s,%s)\"", A, B );
        ts.setValue( value );

        assertEquals( A, ts.getStart() );
        assertEquals( B, ts.getEnd() );

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testEquals() {
        TimeStampRange ts0 = TimeStampRange.fromUntil( A, B );
        TimeStampRange ts1 = TimeStampRange.fromUntil( A, B );
        TimeStampRange ts2 = TimeStampRange.fromUntil( A, C );
        TimeStampRange ts3 = TimeStampRange.fromUntil( B, C );

        assertTrue( "same", ts0.equals( ts0 ) );
        assertTrue( "other", ts0.equals( ts1 ) );
        assertFalse( "unequal", ts0.equals( ts2 ) );
        assertFalse( "unequal", ts0.equals( ts3 ) );

        assertFalse( "string", ts0.equals( "string" ) );

        assertFalse( "null", ts0.equals( null ) );

//        Assert.fail( "method testEquals reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testHashCode() {
        int h0 = TimeStampRange.fromUntil( A, B ).hashCode();
        int h1 = TimeStampRange.fromUntil( A, B ).hashCode();

        assertEquals( "hashes", h0, h1 );
//        Assert.fail( "method testHashCode reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = IllegalArgumentException.class )
    public void testSwappedBounds() {
        TimeStampRange.fromUntil( B, A ).hashCode();

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testUsingDuration() {
        Duration min5 = Duration.of( 5, MINUTES );
        TimeStampRange ts = new TimeStampRange( A, Duration.of( 5, MINUTES ) );
        assertEquals( min5, ts.getLength() );
//        Assert.fail( "method testUsingDuration reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testGetValue() throws SQLException {
        TimeStampRange ts0 = TimeStampRange.fromUntil( A, B );
        String s = ts0.getValue();
        TimeStampRange ts1 = new TimeStampRange();
        ts1.setValue( s );
        
        assertEquals("get value produces parsable value",ts0,ts1);
        
        
//        Assert.fail( "method testGetValue reached end. You know what to do." );
    }
}
