package nl.fontys.sebivenlo.pgtypes;

/**
 * Measure the distance between extremes (including start, excluding end).
 * 
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <T> type of demarcation points
 * @param <U> unit of metering
 */
@FunctionalInterface
public interface MeasureBetween<T, U> {

    /**
     * Compute the distance in unit u
     * @param a start point
     * @param b end point
     * @param u unit of measurement
     * @return the distance or length or time or whatever you want to measure.
     */
    long applyAsLong( T a, T b, U u );
}
