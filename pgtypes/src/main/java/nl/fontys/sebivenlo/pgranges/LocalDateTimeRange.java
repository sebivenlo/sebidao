package nl.fontys.sebivenlo.pgranges;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * PostgreSQL time stamp range (tsrange). This version only supports half open ranges,
 * i.e. '[)' meaning start is included, but end is not.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateTimeRange implements Range<LocalDateTime> {

    /**
     * Helper constructor.
     *
     * @param start time
     * @param noLater end time
     * @return the time range
     * @throws IllegalArgumentException when start is past noLater
     */
    public static LocalDateTimeRange fromUntil( LocalDateTime start, LocalDateTime noLater ) {
        if ( noLater.isBefore( start ) ) {
            throw new IllegalArgumentException( "start must preceed noLater" );
        }
        return new LocalDateTimeRange( start, noLater );
    }
    
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Default constructor to support postgresql jdbc API. This constructor
     * sets start to now() and end to infinity (LocalDateTime.MAX).
     */
    public LocalDateTimeRange() {
        start = LocalDateTime.now();
        end= LocalDateTime.MAX;
    }

    /**
     * Create a range with start and end. A negative duration will use start as
     * end and computes the start of this range by subtracting length from end.
     *
     * @param start of the range
     * @param length length of the range
     */
    public LocalDateTimeRange( LocalDateTime start, Duration length ) {
//        this();
        if ( length.isNegative() ) {
            this.end = start;
            this.start = end.plus( length );
        } else {
            this.start = start;
            this.end = start.plus( length );
        }
    }

    /**
     * Create a range with start and end. If end precedes start, an
     * IllegalArgument exception will be thrown
     *
     * @param start begin
     * @param end of range
     * @throws IllegalArgumentException when end precedes start.
     */
    public LocalDateTimeRange( LocalDateTime start, LocalDateTime end ) {
//        this();
        if ( end.isBefore( start ) ) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Duration getLength() {
        Duration length = Duration.between( start, end );
        return length;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode( this.start );
        hash = 79 * hash + Objects.hashCode( this.end );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final LocalDateTimeRange other = (LocalDateTimeRange) obj;
        if ( !Objects.equals( this.start, other.start ) ) {
            return false;
        }
        if ( !Objects.equals( this.end, other.end ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + start.toString() + "," + end.toString() + ")";
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Check if a range is before a time. If the range is built of times A and
     * B, then the result is true for values of when &gt;= B;
     *
     * @param when the time
     * @return true when there is no overlap and when is after end of this
     * range.
     */
    public boolean isBefore( LocalDateTime when ) {
        return this.end.compareTo( when ) <= 0;
    }

    /**
     * Check if a range is after time. If the range is built of times A and B,
     * then the result is true for values of when &lt;= B;
     *
     * @param when the time
     * @return true when there is no overlap and when is before start of this
     * range.
     */
    public boolean isAfter( LocalDateTime when ) {
        return this.start.compareTo( when ) >= 0;
    }

    @Override
    public long getLength( Object unit ) {
        return start.until( end, (ChronoUnit) unit );
    }

    /**
     * Measure using a.until(b, ChronoUnit)
     *
     * @return the distance between a and b
     */
    @Override
    public MeasureBetween<LocalDateTime, Object> meter() {
        return ( a, b, u ) -> a.until( b, (ChronoUnit) u );
    }

    LocalDateTimeRange setFromString( String value ) { 
        
        String[] part = value.replace( "\"", "" ).replace( "[", "" ).replace( ")", "" ).split( "," );
        this.start = LocalDateTime.parse( part[ 0 ].replace( " ", "T" ) );
        this.end = LocalDateTime.parse( part[ 1 ].replace( " ", "T" ) );
        return this;
    }

    void setBeginEnd( LocalDateTime aStart, LocalDateTime anEnd ) {
        this.start = aStart;
        this.end = anEnd;
    }
}
