package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * Postgres time stamp range (daterange).
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateRange extends PGobject implements Range<LocalDate> {

    /**
     * Helper constructor.
     *
     * @param start time
     * @param noLater end time
     * @return the time range
     * @throws IllegalArgumentException when start is past noLater
     */
    public static LocalDateRange fromUntil( LocalDate start, LocalDate noLater ) {
        if ( noLater.isBefore( start ) ) {
            throw new IllegalArgumentException( "start must preceed noLater" );
        }
        return new LocalDateRange( start, noLater );
    }
    private LocalDate start;
    private LocalDate end;

    /**
     * Default constructor to support postgresql jdbc API. This constructors
     * leaves the fields unset.
     */
    public LocalDateRange() {
        super.setType( "daterange" );
    }

    /**
     * Create a range with start and end. A negative duration will use start as
     * end and computes the start of this range by subtracting length from end.
     *
     * @param start of the range
     * @param length length of the range
     */
    public LocalDateRange( LocalDate start, Period length ) {
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
    public LocalDateRange( LocalDate start, LocalDate end ) {
        this();
        if ( end.isBefore( start ) ) {
            throw new IllegalArgumentException( "start should be before or at end" );
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public LocalDate getStart() {
        return start;
    }

    public Period getLength() {
        Period length = Period.between(start, end );
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
        final LocalDateRange other = (LocalDateRange) obj;
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

    @Override
    public String getValue() {
        return this.toString();
    }

    @Override
    public void setValue( String value ) throws SQLException {
        setType( "daterange" );
        super.setValue( value );
        String[] part = value.replace( "\"", "" ).replace( "[", "" ).replace( ")", "" ).split( "," );
        this.start = LocalDate.parse( part[ 0 ] );
        this.end = LocalDate.parse( part[ 1 ] );
    }

    public LocalDate getEnd() {
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
    public boolean isBefore( LocalDate when ) {
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
    public boolean isAfter( LocalDate when ) {
        return this.start.compareTo( when ) >= 0;
    }
}
