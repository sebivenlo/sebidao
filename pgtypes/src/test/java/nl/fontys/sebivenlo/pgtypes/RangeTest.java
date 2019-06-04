package nl.fontys.sebivenlo.pgtypes;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 * Test the Range interface;
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class RangeTest {

    static class SimpleRange implements Range<Integer> {

        final int start;
        final int end;

        public SimpleRange( int start, int end ) {
            if (end > start) {
            this.start = start;
            this.end = end;
            } else {
                this.end=start;
                this.start=end;
            }
        }

        @Override
        public Integer getStart() {
            return start;
        }

        @Override
        public Integer getEnd() {
            return end;
        }

    }

//    @Ignore( "Think TDD" )
    @Test
    public void overlaps() {
        int a = 10, b = 12, c = 15, d = 17;
        SimpleRange r1 = new SimpleRange( a, b );
        SimpleRange r2 = new SimpleRange( c, d );
        SimpleRange r3 = new SimpleRange( a, c );
        SimpleRange r4 = new SimpleRange( a, d );
        SimpleRange r5 = new SimpleRange( b, d );

        assertFalse( r1.overlaps( r2 ) );
        assertFalse( r2.overlaps( r1 ) );
        assertFalse( r3.overlaps( r2 ) );
        assertFalse( r2.overlaps( r3 ) );
        assertTrue( r4.overlaps( r1 ) );
        assertTrue( r1.overlaps( r4 ) );
        assertTrue( r3.overlaps( r4 ) );
        assertTrue( r4.overlaps( r3 ) );
        assertTrue( r5.overlaps( r3 ) );
        assertTrue( r3.overlaps( r5 ) );

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void methodMeasuresZero() {
        // just for coverage
        int a = 10, b = 12, c = 15, d = 17;
        SimpleRange r1 = new SimpleRange( a, b );
        assertEquals( 0L, r1.meter().applyAsLong( 12, 15, null ) );
//        Assert.fail( "method methodMeasuresZero reached end. You know what to do." );
    }

}
