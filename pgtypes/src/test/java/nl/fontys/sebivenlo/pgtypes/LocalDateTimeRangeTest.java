package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.*;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateTimeRangeTest extends LocalDateTimeRangeTestBase {

//    @Ignore( "Think TDD" )
    @Test
    public void testParse() throws SQLException {
        LocalDateTimeRange ts = new LocalDateTimeRange();
        String value = String.format( "\"[%s,%s)\"", A, B );
        ts.setValue( value );

        assertEquals( A, ts.getStart() );
        assertEquals( B, ts.getEnd() );

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testEquals() {
        LocalDateTimeRange ts0 = LocalDateTimeRange.fromUntil( A, B );
        LocalDateTimeRange ts1 = LocalDateTimeRange.fromUntil( A, B );
        LocalDateTimeRange ts2 = LocalDateTimeRange.fromUntil( A, C );
        LocalDateTimeRange ts3 = LocalDateTimeRange.fromUntil( B, C );

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
        int h0 = LocalDateTimeRange.fromUntil( A, B ).hashCode();
        int h1 = LocalDateTimeRange.fromUntil( A, B ).hashCode();

        assertEquals( "hashes", h0, h1 );
//        Assert.fail( "method testHashCode reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = IllegalArgumentException.class )
    public void testSwappedBounds() {
        LocalDateTimeRange.fromUntil( B, A ).hashCode();

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testUsingDuration() {
        Duration min5 = Duration.of( 5, MINUTES );
        LocalDateTimeRange ts = new LocalDateTimeRange( A, Duration.of( 5, MINUTES ) );
        assertEquals( min5, ts.getLength() );
//        Assert.fail( "method testUsingDuration reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testGetValue() throws SQLException {
        LocalDateTimeRange ts0 = LocalDateTimeRange.fromUntil( A, B );
        String s = ts0.getValue();
        LocalDateTimeRange ts1 = new LocalDateTimeRange();
        ts1.setValue( s );

        assertEquals( "get value produces parsable value", ts0, ts1 );

//        Assert.fail( "method testGetValue reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = IllegalArgumentException.class )
    public void startBeforeEndoNotAccepted() {
        LocalDateTimeRange ts0 = new LocalDateTimeRange( B, A );

//        Assert.fail( "method startBeforeEndoNotAccepted reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void negativeDurationStartEarlier() {
        LocalDateTimeRange ts0 = new LocalDateTimeRange( B, Duration.of( -30, MINUTES ) );

        assertEquals( "should produce test value A", A, ts0.getStart() );
//        Assert.fail( "method negativeDurationStartEarlier reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testBefore() {
        LocalDateTimeRange ts0 = new LocalDateTimeRange( A, Duration.of( -30, MINUTES ) );
        System.out.println( "ts0 = " + ts0 );
        assertTrue( "should be before", ts0.isBefore( B ) );
//        Assert.fail( "method testIsAfter reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testAfter() {
        LocalDateTimeRange ts0 = new LocalDateTimeRange( A, B );//Duration.of( -30, MINUTES ) );
        System.out.println( "ts0 = " + ts0 );
        assertTrue( "should be before", ts0.isAfter( A ) );
//        Assert.fail( "method testIsAfter reached end. You know what to do." );
    }

}
