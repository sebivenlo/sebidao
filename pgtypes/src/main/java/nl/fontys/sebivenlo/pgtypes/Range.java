package nl.fontys.sebivenlo.pgtypes;

import java.util.Comparator;
import java.util.function.ToLongBiFunction;

/**
 * The range's reason of existence is testing for overlaps.
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
     * @param a first point
     * @param b second
     * @param inBetween point
     * @return true if inBetween is at or after a and before b
     */
    default boolean isBetween( T a, T b, T inBetween ) {
        System.out.print( "a = " + a );
        System.out.print( " b = " + b );
        System.out.print( " inBetween = " + inBetween );
        boolean result = a.compareTo( inBetween ) <= 0 && inBetween.compareTo( b ) < 0;
        System.out.println( " result = " + result );
        return result;
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
    }

    /**
     * Do two ranges overlap. Overlap means the the ranges have a non-zero sub-range in common.
     * @param r1 first range
     * @param r2 second range
     * @return true if there is a non zero overlap
     */
    default boolean overlaps( Range<T> r1, Range<T> r2 ) {
        T a, b, c;
        if ( r1.getStart().compareTo( r2.getStart() ) <= 0 ) {
            a = r1.getStart();
            b = r1.getEnd(); // a <=b
            c = r2.getStart();
        } else {
            a = r2.getStart();
            b = r2.getEnd(); // c<=d 
            c = r1.getStart();
        }

        return a.compareTo( c) <= 0 && c.compareTo( b) < 0;
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
     * @return the distance from a to b.
     */
    MeasureBetween<T, Object> meter();

}
