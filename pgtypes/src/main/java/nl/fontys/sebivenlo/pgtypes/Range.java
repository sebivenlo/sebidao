package nl.fontys.sebivenlo.pgtypes;

import java.util.Comparator;

/**
 * A range reason of existence is testing for overlaps.
 *
 * The range should comply to the invariant that the start is less or equal its
 * end.
 *
 * This range definition is of the half open type. The start is included in the range, the
 * end is not. The typical notation for half open ranges of this kind is
 * [start,end), indeed with two different brackets.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <T> the demarcation type. Type of start and end.
 */
public interface Range<T extends Comparable<?>> {

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
}
