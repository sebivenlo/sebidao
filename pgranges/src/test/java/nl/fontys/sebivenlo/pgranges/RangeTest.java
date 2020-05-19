package nl.fontys.sebivenlo.pgranges;

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

        @Override
        public MeasureBetween<Integer, Object> meter() {
            return (a,b,u) -> (long) b-a;
        }

    }

//    @Ignore( "Think TDD" )
    @Test
    public void overlaps() {
        int a = 10, b = 22, c = 35, d = 47;
        SimpleRange ab = new SimpleRange( a, b );
        SimpleRange cd = new SimpleRange( c, d );
        SimpleRange ac = new SimpleRange( a, c );
        SimpleRange ad = new SimpleRange( a, d );
        SimpleRange bd = new SimpleRange( b, d );

        assertFalse( ab.overlaps( cd ) );
        assertFalse( cd.overlaps( ab ) );
        assertFalse( cd.overlaps( ac ) );
        assertFalse( ac.overlaps( cd ) );
        assertTrue( ad.overlaps( ab ) );
        assertTrue( ab.overlaps( ad ) );
        assertTrue( ac.overlaps( ad ) );
        assertTrue( ad.overlaps( ac ) );
        assertTrue( bd.overlaps( ac ) );
        assertTrue( ac.overlaps( bd ) );

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void methodMeasuresZero() {
        // just for coverage
        int a = 10, b = 12, c = 15, d = 17;
        SimpleRange r1 = new SimpleRange( a, b );
        assertEquals( 0L, r1.meter().applyAsLong( 12, 12, null ) );
//        Assert.fail( "method methodMeasuresZero reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void isBetween() {
        int a = 10, b = 22, c = 35, d = 47;
        SimpleRange r1 = new SimpleRange( a, b );

        assertFalse("abc", r1.isBetween( a, b, c ));
        assertFalse("abc", r1.isBetween( a, b, d ));
        assertFalse("abc", r1.isBetween( c, d, d ));
        assertTrue("abc", r1.isBetween( a, c, b ));
        assertTrue("abc", r1.isBetween( a, b, a ));
//        Assert.fail( "method isBetween reached end. You know what to do." );
    }
}
