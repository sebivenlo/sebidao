
package nl.fontys.sebivenlo.pgranges;

import nl.fontys.sebivenlo.pgranges.MeasureBetween;
import nl.fontys.sebivenlo.pgranges.Range;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LongRange implements Range<Long> {

    final Long start;
    final Long end;

    public LongRange( long start, long end ) {
        if ( start <= end ) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    @Override
    public Long getStart() {
        return start;
    }

    @Override
    public Long getEnd() {
        return end;
    }

    @Override
    public MeasureBetween<Long, Object> meter() {
        return (a,b,u) -> b-a;
    }

}
