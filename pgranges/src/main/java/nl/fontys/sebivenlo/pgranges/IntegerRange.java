package nl.fontys.sebivenlo.pgranges;

import nl.fontys.sebivenlo.pgranges.MeasureBetween;
import nl.fontys.sebivenlo.pgranges.Range;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class IntegerRange implements Range<Integer> {

    final Integer start;
    final Integer end;

    public IntegerRange( int start, int end ) {
        if ( start > end ) {
            this.end = start;
            this.start = end;
        } else {
            this.start = start;
            this.end = end;
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
        return ( a, b, u ) -> b - a;
    }

}
