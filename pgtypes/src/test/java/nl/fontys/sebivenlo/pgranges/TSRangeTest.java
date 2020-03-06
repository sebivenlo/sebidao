package nl.fontys.sebivenlo.pgranges;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TSRangeTest {

    //@Disabled
    @Test
    void getSQLTypeName() throws SQLException {
        TSRange tsRange = new TSRange();

        assertThat( tsRange.getSQLTypeName().toLowerCase() )
                .isEqualTo( "tsrange" );
//        fail( "test Method reached it's and. You will know what to do." );
    }

    //@Disabled
    @Test
    void fromUntil() {
        LocalDateTime start = LocalDateTime.of( LocalDate.now(), LocalTime
                                                .of( 12, 0 ) );
        LocalDateTime end = start.plusHours( 4 );
        TSRange ts = TSRange.fromUntil( start, end );
        assertThat( ts.getLength() ).isEqualTo( Duration.ofHours( 4 ) );
//        fail( "test fromUntil reached it's and. You will know what to do." );
    }

//    @Mock
//    SQLOutput output;
    /**
     * Test writing to SQLOutput
     */
    //@Disabled
    @Test
    void testOut() throws SQLException {
        SQLOutput output = Mockito.mock( SQLOutput.class );
        LocalDateTime start = LocalDateTime.of( LocalDate.now(), LocalTime
                                                .of( 12, 0 ) );
        LocalDateTime end = start.plusHours( 4 );
        TSRange ts = TSRange.fromUntil( start, end );
        ts.writeSQL( output );
        String expectedString = "[" + start.toString().replace( "T", " " ) + "," + end
                .toString().replace( "T", " " ) + ")";
        verify( output, times( 1 ) ).writeString( expectedString );

//        fail( "test testOut reached it's and. You will know what to do." );
    }

    //@Disabled
    @Test
    void testIn() throws SQLException {
        SQLInput input = Mockito.mock( SQLInput.class );
//        input.readString();
        String i = "[\"2019-05-05 19:30:15\",\"2019-05-06 08:30:00\")";
        System.out.println( "i = " + i);
        Mockito.when( input.readString() ).thenReturn( i );

        TSRange ts = new TSRange();
        ts.readSQL( input, "tsrange" );
        assertThat( ts.getStart().toString() ).isEqualTo(  "2019-05-05T19:30:15"  );
//        fail( "test testIn reached it's and. You will know what to do." );
    }

}
