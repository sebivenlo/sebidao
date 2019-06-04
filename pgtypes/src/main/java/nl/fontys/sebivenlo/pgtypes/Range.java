package nl.fontys.sebivenlo.pgtypes;

import java.util.Comparator;
import java.util.function.ToLongBiFunction;

/**
 * A range reason of existence is testing for overlaps.
 *
 * The range should comply to the invariant that the start is less or equal it's
 * end. Depending on the type, this can mean start is (left of, before, lower)
 * than end, or equal to end.
 *
 * This range definition is of the <i>half open</i> type. The start is included
 * in the range, the end is not. The mathematical notation for half open ranges
 * of this kind is [start,end), indeed with two different brackets. The square
 * bracket is the closed end, the parenthesis is at the open end.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <T> the demarcation type. Type of start and end.
 */
public interface Range<T extends Comparable<? super T>> {

    /**
     * Get the start demarcation of this range. Start is part of this range.
     *
     * @return start
     */
    T getStart();

    /**
     * Get the end demarcation of this range. All points in the range are before
     * (less than) the end of this range.
     *
     * @return end
     */
    T getEnd();

    /**
     * Is a point contained in this range.
     *
     * @param point the point
     * @return true is point not before start and not after end.
     */
    default boolean contains( T point ) {
        return getStart().compareTo( point ) <= 0
                && getEnd().compareTo( point ) > 0;
    }

    /**
     * Is a midpoint between a (inclusive) and b (exclusive).
     *
     * @param <U>
     * @param a
     * @param b
     * @param inBetween
     * @return
     */
    default boolean isBetween( T a, T b, T inBetween ) {
        return a.compareTo( inBetween ) <= 0 && b.compareTo( inBetween ) > 0;
    }

    /**
     * Does this range overlap with another one. The overlap condition can also
     * be tested in the database with a check constraint. See @see
     * <a href='https://www.postgresql.org/docs/11/rangetypes.html#RANGETYPES-CONSTRAINT'>Range
     * Type constraints</a>
     *
     * @param other to check
     * @return true on overlap with other
     */
    default boolean overlaps( Range< T> other ) {
        return overlaps( this, other );
//
//        Comparable a = this.getStart();
//        Comparable b = this.getEnd(); // a <=b
//        Comparable c = other.getStart();
//        Comparable d = other.getEnd(); // c<=d 
//
//        boolean abc = isBetween( a, b, c );
//        boolean abd = isBetween( a, b, d );
//        boolean cda = isBetween( c, d, a );
//        boolean cdb = isBetween( c, d, b );
//        System.out.println( abc + "," + abd + "," + cda + "," + cdb );
//
//        boolean result = ( abc && !abd )
//                || ( cda && !cdb );
//        return result;
    }

    default boolean overlaps( Range<T> r1, Range<T> r2 ) {

        T a = r1.getStart();
        T b = r1.getEnd(); // a <=b
        T c = r2.getStart();
        T d = r2.getEnd(); // c<=d 

        boolean abc = isBetween( a, b, c );
        boolean abd = isBetween( a, b, d );
        boolean cda = isBetween( c, d, a );
        boolean cdb = isBetween( c, d, b );
        System.out.println( "a=" + a + " b=" + b + " c=" + c + " d=" + d );
        System.out.println( "abc      abd    , cda     ,cdb" );
        System.out.println( abc + ",   " + abd + ",   " + cda + ",   " + cdb );

        boolean result = abc || abd
                || cda || cdb;
        return result;
    }

    /**
     * Get the length of this range in some unit. The effective operation is
     * (end-start), but since we do not know how to subtract the subtypes, that
     * is left to the implementer. The exception thrown on incompatibility of
     * range and unit is also left to the implementer.
     *
     * @param unit of measurement
     * @return the length
     * @throws RuntimeException when the unit and this range are not compatible
     */
    default long getLength( Object unit ) {
        return meter().applyAsLong( this.getStart(), this.getEnd(), unit );
    }

    /**
     * Compute the length that this and an other range overlap.
     *
     * @param other range
     * @param unit of measurement
     * @return the length of the overlap
     */
    default long overlap( Range<? extends T> other, Object unit ) {
        T s1 = this.getStart();
        T s2 = other.getStart();
        T e1 = this.getEnd();
        T e2 = other.getEnd();
        T rightMostStart = s1.compareTo( s2 ) > 0 ? s1 : s2;
        T leftMostEnd = e1.compareTo( e2 ) < 0 ? e1 : e2;
        if ( e1.compareTo( s2 ) < 0 || e2.compareTo( s1 ) < 0 ) {
            return 0L;
        }

        return meter().applyAsLong( rightMostStart, leftMostEnd, unit );
    }

    /**
     * Get the method to determine distances between points.
     *
     * This method needs to be overwritten, because the default is just a
     * placeholder.
     *
     * @return the distance from a to b.
     */
    default MeasureBetween<T, Object> meter() {

        return ( a, b, c ) -> 0L;
    }

}
