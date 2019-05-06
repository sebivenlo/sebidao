package nl.fontys.sebivenlo.dao.pg;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.SQLType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * Postgresql tsrange as a java object. Takes care of (un)marshalling.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TsRange extends PGobject {

    private LocalDateTime start;
    private LocalDateTime end;

    public TsRange() {
        super.setType( "tsrange" );
        System.out.println( "default ctor" );
    }

    public TsRange( LocalDateTime start, Duration length ) {
        super.setType( "tsrange" );
        this.start = start;
        this.end = start.plus( length );
    }

    public TsRange( LocalDateTime start, LocalDateTime end ) {
        super.setType( "tsrange" );
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
        final TsRange other = (TsRange) obj;
        if ( !Objects.equals( this.start, other.start ) ) {
            return false;
        }
        if ( !Objects.equals( this.end, other.end ) ) {
            return false;
        }
        return true;
    }

    public boolean overlaps( TsRange other ) {
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
        return true;
    }

    @Override
    public String toString() {
        return "[" + start.toString() + "," + end.toString() + ")";
    }

    @Override
    public String getValue() {
        System.out.println( "get Value " + this );
        return this.toString();
    }

    @Override
    public void setValue( String value ) throws SQLException {
        setType( "tsrange" );
        super.setValue( value );
        System.out.println( "setter " + value );
        String[] part = value.replace( "\"", "" ).replace( "[", "" ).replace( ")", "" ).split( "," );
        this.start = LocalDateTime.parse( part[ 0 ].replace( " ", "T" ) );
        this.end = LocalDateTime.parse( part[ 1 ].replace( " ", "T" ) );
        System.out.println( "parsed start = " + start );
        System.out.println( "parsed end = " + end );
    }
}
