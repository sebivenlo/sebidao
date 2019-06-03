package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.Period;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.*;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateRangeTest {// extends LocalDateRangeTestBase {

    static Clock clock = new Clock() {
        @Override
        public ZoneId getZone() {
            return systemDefault();
        }

        @Override
        public Clock withZone( ZoneId zone ) {

            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochSecond( 1557223200L );
        }
    };

    static final LocalDate A = now( clock ).minus( 2, WEEKS );
    static final LocalDate B = A.plus( 3, DAYS );
    static final LocalDate C = B.plus( 10, DAYS );
    static final LocalDate D = C.plus( 1, WEEKS );
//    @Ignore( "Think TDD" )

    @Test
    public void testParse() throws SQLException {
        LocalDateRange ts = new LocalDateRange();
        String value = String.format( "\"[%s,%s)\"", A, B );
        ts.setValue( value );

        assertEquals( A, ts.getStart() );
        assertEquals( B, ts.getEnd() );

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testEquals() {
        LocalDateRange ts0 = LocalDateRange.fromUntil( A, B );
        LocalDateRange ts1 = LocalDateRange.fromUntil( A, B );
        LocalDateRange ts2 = LocalDateRange.fromUntil( A, C );
        LocalDateRange ts3 = LocalDateRange.fromUntil( B, C );

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
        int h0 = LocalDateRange.fromUntil( A, B ).hashCode();
        int h1 = LocalDateRange.fromUntil( A, B ).hashCode();

        assertEquals( "hashes", h0, h1 );
//        Assert.fail( "method testHashCode reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = IllegalArgumentException.class )
    public void testSwappedBounds() {
        LocalDateRange.fromUntil( B, A ).hashCode();

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testUsingDuration() {
        Period day5 = Period.ofDays( 5 );
        LocalDateRange ts = new LocalDateRange( A, day5 );
        assertEquals( day5, ts.getLength() );
//        Assert.fail( "method testUsingDuration reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testGetValue() throws SQLException {
        LocalDateRange ts0 = LocalDateRange.fromUntil( A, B );
        String s = ts0.getValue();
        LocalDateRange ts1 = new LocalDateRange();
        ts1.setValue( s );

        assertEquals( "get value produces parsable value", ts0, ts1 );

//        Assert.fail( "method testGetValue reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = IllegalArgumentException.class )
    public void startBeforeEndoNotAccepted() {
        LocalDateRange ts0 = new LocalDateRange( B, A );

//        Assert.fail( "method startBeforeEndoNotAccepted reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void negativeDurationStartEarlier() {
        LocalDateRange ts0 = new LocalDateRange( B, Period.ofDays( -3 ) );

        assertEquals( "should produce test value A", A, ts0.getStart() );
//        Assert.fail( "method negativeDurationStartEarlier reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testBefore() {
        LocalDateRange ts0 = new LocalDateRange( A, Period.ofDays( -3 ) );
        System.out.println( "ts0 = " + ts0 );
        assertTrue( "should be before", ts0.isBefore( B ) );
//        Assert.fail( "method testIsAfter reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testAfter() {
        LocalDateRange ts0 = new LocalDateRange( A, B );
        System.out.println( "ts0 = " + ts0 );
        assertTrue( "should be before", ts0.isAfter( A ) );
//        Assert.fail( "method testIsAfter reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testPointInRange() {
        LocalDateRange range = new LocalDateRange( A, B );
        LocalDate A1 = A.plusDays( A.until( B, DAYS )/2 );
        assertTrue( range.contains( A1 ) );

//        Assert.fail( "method testPointInRange reached end. You know what to do." );
    }
//    @Ignore( "Think TDD" )
    @Test
    public void startPointInRange() {
        LocalDateRange range = new LocalDateRange( A, B );

        assertTrue( range.contains( A ) );
//        Assert.fail( "method startPointInRange reached end. You know what to do." );
    }
    
//    @Ignore( "Think TDD" )
    @Test
    public void endPointNotInRange() {
        LocalDateRange range = new LocalDateRange( A, B );

        assertFalse( range.contains( B ) );
//        Assert.fail( "method endPointNotInRange reached end. You know what to do." );
    }
}
