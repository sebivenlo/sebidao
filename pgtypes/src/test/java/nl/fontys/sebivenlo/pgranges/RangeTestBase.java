/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.pgranges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )

public abstract class RangeTestBase<T extends Comparable<? super T>, R extends Range<T>> {

    abstract T a();

    abstract T b();

    abstract T c();

    abstract T d();

    abstract R createRange( T start, T end );

    abstract Object measurementUnit();

    T[] times = (T[]) new Comparable[] { a(), b(), c(), c() };
    static Object[][] testData = {
        { false, "abcd" },
        { true, "acbd" },
        { true, "adbc" },
        { false, "abbc" },
        { false, "babc" },
    };

    String label;
    boolean expected;

    public RangeTestBase( boolean expected, String label ) {
        this.label = label;
        this.expected = expected;
    }

    @Parameterized.Parameters( name = "{index}: overlaps {0} with {1}" )
    public static Object[][] data() {
        return testData;
    }

    @Test
    public void testOverlap() {
        int i0 = label.charAt( 0 ) - 'a';
        int i1 = label.charAt( 1 ) - 'a';
        int i2 = label.charAt( 2 ) - 'a';
        int i3 = label.charAt( 3 ) - 'a';
        T t1 = times[ i0 ];
        T t2 = times[ i1 ];
        T t3 = times[ i2 ];
        T t4 = times[ i3 ];

        Range<T> ts1 = createRange( t1, t2 );
        Range<T> ts2 = createRange( t3, t4 );
        assertEquals( "overlaps" + expected + ts1 + "<>" + ts2, expected, ts1.overlaps( ts2 ) );
        assertEquals( "overlaps" + expected + ts2 + "<>" + ts1, expected, ts2.overlaps( ts1 ) );

    }

//    @Ignore( "Think TDD" )
    @Test
    public void testLength() {
        assertTrue( createRange( a(), b() ).getLength( measurementUnit() ) > 0 );

//        Assert.fail( "method testLength reached end. You know what to do." );
    }
    
//    @Ignore( "Think TDD" )
    @Test
    public void normalizes() {
        assertTrue( createRange( b(), a() ).getLength( measurementUnit() ) > 0 );
    }
    
//    @Ignore( "Think TDD" )
    @Test
    public void containsTest() {
        // coverage
        R r = createRange( b(), c());
        assertFalse("a not",r.contains( a()));
        assertFalse("c not",r.contains( c()));
        assertFalse("d not",r.contains( d()));
        
//        Assert.fail( "method containsTest reached end. You know what to do." );
        
    }
}
