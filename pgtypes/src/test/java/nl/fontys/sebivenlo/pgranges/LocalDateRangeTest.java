package nl.fontys.sebivenlo.pgranges;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import java.time.Period;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.*;
import java.time.temporal.TemporalQuery;
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
            return Instant.ofEpochSecond( origin );
        }
    };

    static class FixedClock extends Clock {

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
            return Instant.ofEpochSecond( origin );
        }

    }

    // 1999-1-1:11:25:13;
    static long origin = of( 1999, 1, 1 ).toEpochDay() * 24 * 3600 + 11 * 60 * 60 + 25 * 60 + 13;

    static final LocalDate A = now( clock ).minus( 2, WEEKS );
    static final LocalDate B = A.plus( 13, DAYS );
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
        assertEquals( 5, ts.getLength( DAYS ) );
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
    public void rangeIsNormalised() {
        assertEquals( new LocalDateRange( A, B ), new LocalDateRange( B, A ) );

//        Assert.fail( "method rangeIsNormalised reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void negativeDurationStartEarlier() {
        LocalDateRange ts0 = new LocalDateRange( B, Period.ofDays( -13 ) );

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
        assertTrue( "should be after", ts0.isAfter( A ) );
        assertFalse( "should not be after", ts0.isAfter( B ) );
//        Assert.fail( "method testIsAfter reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void testPointInRange() {
        LocalDateRange range = new LocalDateRange( A, B );
        LocalDate A1 = A.plusDays( A.until( B, DAYS ) / 2 );
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
    public void getPeriod() {
        LocalDateRange range = new LocalDateRange( B, A ); // flipped to check normalize
        System.out.println( "A = " + A );

        System.out.println( "B = " + B );
        long p = range.getLength( ChronoUnit.DAYS );
        System.out.println( "p = " + p );
        assertEquals( 13, p );
//        Assert.fail( "method getPeriod reached end. You know what to do." );
    }
//    @Ignore( "Think TDD" )

    @Test
    public void getLength() {
        LocalDateRange range = new LocalDateRange( B, C );

        assertEquals( 10, range.getDays() );

        LocalDate d1 = of( 2019, 4, 12 );
        LocalDate d2 = of( 2019, 5, 28 );
        range = new LocalDateRange( d1, d2 );
        assertEquals( 16 + 30, range.getDays() );
//        Assert.fail( "method getLength reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void intersections() {
        LocalDateRange range1 = new LocalDateRange( A, B );
        LocalDateRange range2 = new LocalDateRange( C, D );
        assertEquals( 0, range1.overlap( range2, DAYS ) );

        LocalDateRange range3 = new LocalDateRange( A, D );
        assertEquals( range1.getLength( DAYS ), range3.overlap( range1, DAYS ) );
        LocalDateRange range4 = new LocalDateRange( B, C );
        assertEquals( range4.getLength( DAYS ), range3.overlap( range4, DAYS ) );
        assertEquals( range4.getLength( DAYS ), range4.overlap( range3, DAYS ) );
//        Assert.fail( "method intersections reached end. You know what to do." );
    }
}
