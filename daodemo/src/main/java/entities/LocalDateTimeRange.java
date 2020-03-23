package entities;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateTimeRange {

    final LocalDateTime start;
    final LocalDateTime end;
    final String brackets;

    public LocalDateTimeRange( LocalDateTime start, LocalDateTime end ) {
        this( start, end, "[)" );
    }

    public LocalDateTimeRange( LocalDateTime start, Duration dur  ) {
        this( start, start.plus( dur), "[)" );
    }

    public LocalDateTimeRange( LocalDateTime start, LocalDateTime end, String brackets ) {
        this.start = start;
        this.end = end;
        this.brackets = brackets;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getBrackets() {
        return brackets;
    }

    @Override
    public String toString() {
        return "[\"" + start + "\",\"" + end + "\")";
    }

    public static LocalDateTimeRange fromTSRangeObject( Object object ) {
        String tss = object.toString();
        String bracket0 = tss.substring( 0, 1 );
        String bracket1 = tss.substring( tss.length() - 1, tss.length() );
        String[] split = tss.replaceAll( "[]\\[()\"]", "" ).replaceAll( " ", "T" ).split( "," );

        LocalDateTime t0 = LocalDateTime.parse( split[ 0 ] );
        LocalDateTime t1 = LocalDateTime.parse( split[ 1 ] );
        LocalDateTimeRange result = new LocalDateTimeRange( t0, t1, bracket0 + bracket1 );
        return result;
    }

    public static void main( String[] args ) {
        String s = "[\"2020-03-18 12:45:43.039368\",\"2022-03-18 12:45:43.039368\")";
        System.out.println( "s = " + s );
        LocalDateTimeRange fromTSRangeString = fromTSRangeObject( s );

        System.out.println( "fromTSRangeString = " + fromTSRangeString );
    }
}
