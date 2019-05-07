package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * Postgres time stamp range (tsrange).
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TimeStampRange extends PGobject {

    /**
     * Helper constructor.
     *
     * @param start time
     * @param noLater end time
     * @return the time range
     * @throws IllegalArgumentException when start is past noLater
     */
    public static TimeStampRange fromUntil( LocalDateTime start, LocalDateTime noLater ) {
        if ( noLater.isBefore( start ) ) {
            throw new IllegalArgumentException( "start must preceed noLater" );
        }
        return new TimeStampRange( start, noLater );
    }
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Default constructor to support postgresql jdbc API. This constructors
     * leaves the fields unset.
     */
    public TimeStampRange() {
        super.setType( "tsrange" );
    }

    /**
     * Create a range with start and end. A negative duration will use start as
     * end and computes the start of this range by subtracting length from end.
     *
     * @param start of the range
     * @param length length of the range
     */
    public TimeStampRange( LocalDateTime start, Duration length ) {
        this();
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
    public TimeStampRange( LocalDateTime start, LocalDateTime end ) {
        this();
        if ( end.isBefore( start ) ) {
            throw new IllegalArgumentException( "start should be before or at end" );
        }
        this.start = start;
        this.end = end;
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
        final TimeStampRange other = (TimeStampRange) obj;
        if ( !Objects.equals( this.start, other.start ) ) {
            return false;
        }
        if ( !Objects.equals( this.end, other.end ) ) {
            return false;
        }
        return true;
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
    public boolean overlaps( TimeStampRange other ) {
        if ( this.end.isBefore( other.start ) ) {
            return false;
        }

        if ( other.end.isBefore( this.start ) ) {
            return false;
        }

        if ( start.isBefore( other.start ) && this.end.isAfter( other.start ) ) {
            return true;
        }
        if ( other.start.isBefore( this.start ) && other.end.isAfter( this.start ) ) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + start.toString() + "," + end.toString() + ")";
    }

    @Override
    public String getValue() {
        return this.toString();
    }

    @Override
    public void setValue( String value ) throws SQLException {
        setType( "tsrange" );
        super.setValue( value );
        String[] part = value.replace( "\"", "" ).replace( "[", "" ).replace( ")", "" ).split( "," );
        this.start = LocalDateTime.parse( part[ 0 ].replace( " ", "T" ) );
        this.end = LocalDateTime.parse( part[ 1 ].replace( " ", "T" ) );
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Check if a range is before a time.
     * If the range is build of times A and B, then the result is true for values >= B;
     * @param when the time
     * @return true when there is no overlap and when is after end of this range.
     */
    public boolean isBefore( LocalDateTime when ) {
        return this.end.compareTo( when ) <=0;
    }

    public boolean isAfter( LocalDateTime when ) {
        return this.start.compareTo(when )>=0;
    }
}
