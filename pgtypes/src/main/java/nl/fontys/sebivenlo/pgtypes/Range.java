package nl.fontys.sebivenlo.pgtypes;

import java.util.Comparator;
import java.util.function.ToLongBiFunction;

/**
 * A range reason of existence is testing for overlaps.
 *
 * The range should comply to the invariant that the start is less or equal its
 * end.
 *
 * This range definition is of the half open type. The start is included in the
 * range, the end is not. The typical notation for half open ranges of this kind
 * is [start,end), indeed with two different brackets.
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
     * Get the end demarcation of this range. All point in the range are before
     * (less than) the end of this range.
     *
     * @return end
     */
    T getEnd();

    default boolean contains( T point ) {
        return getStart().compareTo( point ) <= 0
                && getEnd().compareTo( point ) > 0;
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
    default boolean overlaps( Range<T> other ) {

        Comparable a = this.getStart();
        Comparable b = this.getEnd(); // a <=b
        Comparable c = other.getStart();
        Comparable d = other.getEnd(); // c<=d 

        if ( b.compareTo( c ) < 0 ) {
            return false;
        }

        if ( d.compareTo( a ) < 0 ) {
            return false;
        }

        if ( a.compareTo( c ) < 0 && b.compareTo( c ) > 0 ) {
            return true;
        }

        if ( c.compareTo( a ) < 0 && d.compareTo( a ) > 0 ) {
            return true;
        }
        return false;
    }

    /**
     * Get the length of this range in some unit. The effective operation is
     * (end-start), but since we do not know how to subtract the subtypes, that
     * is left to the implementor.
     *
     * @param unit of measurement
     * @return the length
     */
    long getLength( Object unit );

    /**
     * Compute the length that this and an other range overlap.
     *
     * @param other range
     * @param unit of measurement
     * @return the length of the intersection
     */
    default long intersection( Range<? extends T> other, Object unit ) {
        T s1 = this.getStart();
        T s2 = other.getStart();
        T e1 = this.getEnd();
        T e2 = other.getEnd();
        T rightMostStart = s1.compareTo( s2 ) > 0 ? s1 : s2;
        T leftMostEnd = e1.compareTo( e2 ) < 0 ? e1 : e2;
        if ( e1.compareTo( s2)< 0 || e2.compareTo(e1) < 0 ) {
           return 0L;
       }

        return meter().applyAsLong( rightMostStart, leftMostEnd, unit);
//        return 0L;
    }

    /**
     * Get the method to determine distances between points.
     *
     * @return
     */
    default MeasureBetween<T, Object> meter() {
        return ( a, b, c ) -> 0L;
    }

}
