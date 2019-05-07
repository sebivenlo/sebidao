package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.*;
import static nl.fontys.sebivenlo.pgtypes.TimeStampRange.fromUntil;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )
public class TimeStampRangeOverlapTest extends TimeStampTestBase {

   
    LocalDateTime[] times = { A, B, C, D };
    static Object[][] testData = {
        { false, "abcd" },
        { true, "acbd" },
        { true, "adbc" },
        {false, "abbc"}
    };

    String label;
    boolean expected;

    public TimeStampRangeOverlapTest( boolean expected, String label ) {
        this.label = label;
        this.expected = expected;
    }

    @Parameterized.Parameters( name = "{index}: overlaps {0} with {1}" )
    public static Object[][] data() {
        return testData;
    }

    @Test
    public void testOverlap() {
        int i0 = label.charAt( 0 ) - 'a';
        int i1 = label.charAt( 1 ) - 'a';
        int i2 = label.charAt( 2 ) - 'a';
        int i3 = label.charAt( 3 ) - 'a';
        LocalDateTime t1 = times[ i0 ];
        LocalDateTime t2 = times[ i1 ];
        LocalDateTime t3 = times[ i2 ];
        LocalDateTime t4 = times[ i3 ];

        TimeStampRange ts1 = fromUntil( t1, t2 );
        TimeStampRange ts2 = fromUntil( t3, t4 );
        assertEquals( "overlaps" + expected + ts1 + "<>" + ts2, expected, ts1.overlaps( ts2 ) );
        assertEquals( "overlaps" + expected + ts2 + "<>" + ts1, expected, ts2.overlaps( ts1 ) );

    }
}
