package nl.fontys.sebivenlo.pgranges;

import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.time.Month;
import static java.time.Month.*;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.List;
import java.util.Map;
import static nl.fontys.sebivenlo.pgranges.Seasons.PriceClass.*;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static nl.fontys.sebivenlo.pgranges.Seasons.THIS_YEAR;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class SeasonTest {

    Seasons ses = new Seasons();
//    @Ignore( "Think TDD" ) 

    @Test
    public void feb28IsMid() {
        assertEquals( MID, ses.getPriceClass( LocalDate.of( THIS_YEAR, 2, 28 ) ) );
//        Assert.fail( "method feb28IsMid reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void crocusCosts700() {
        LocalDateRange crocus = LocalDateRange.fromUntil( of( THIS_YEAR, 2, 20 ), of( THIS_YEAR, 2, 25 ) );

        assertEquals( "5 day at 140", 700L, ses.getPrice( crocus ) );
//        Assert.fail( "method crocusCosts700 reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void extendedCrocusCosts() {
        LocalDate start = of( THIS_YEAR, 2, 20 );
        LocalDate end = of( THIS_YEAR, MARCH, 8 );
        LocalDateRange crocus = LocalDateRange.fromUntil( start, end );
        long part1 = DAYS.between( start, of( THIS_YEAR, MARCH, 1 ) );
        long part2 = DAYS.between( of( THIS_YEAR, MARCH, 1 ), end );

        long expected = ses.getDayPrice( MID ) * part1 + ses.getDayPrice( LOW ) * part2;
        assertEquals( expected, ses.getPrice( crocus ) );

//        Assert.fail( "method extendedCrocusCosts reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void reallyLongHolidayCostsPlenty() {
        LocalDate start = of( THIS_YEAR, 2, 20 );
        LocalDate end = of( THIS_YEAR, JUNE, 8 );
        LocalDateRange longHoliday = LocalDateRange.fromUntil( start, end );

        long e1
                = DAYS.between( start, of( THIS_YEAR, MARCH, 1 ) ) * ses.getDayPrice( MID );
        System.out.println( "e1 = " + e1 );
        long e2 = of( THIS_YEAR, MARCH, 1 ).until( of( THIS_YEAR, MAY, 1 ), DAYS ) * ses.getDayPrice( LOW ); // march april
        System.out.println( "e2 = " + e2 );
        long e3 = of( THIS_YEAR, MAY, 1 ).until(of( THIS_YEAR, MAY, 15 ),DAYS ) * ses.getDayPrice( MID ); // may
        System.out.println( "e3 = " + e3 );
        long e4 = of( THIS_YEAR, MAY, 15 ).until( of( THIS_YEAR, JUNE, 8 ),DAYS ) * ses.getDayPrice( HIGH ); // 1/2 may-1/2 jun
        System.out.println( "e4 = " + e4 );
        long price = ses.getPrice( longHoliday );
        System.out.println( "price = " + price );
        assertEquals( "hefty", e1 + e2 + e3 + e4, price );
//        Assert.fail( "method reallyLongHolidayCostsPlenty reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void seasonsBetween() {
        LocalDate start = of( THIS_YEAR, 2, 20 );
        LocalDate end = of( THIS_YEAR, JUNE, 8 );
        LocalDate endOfSummer = of( THIS_YEAR, Month.SEPTEMBER, 12 );

        List<Map.Entry<LocalDateRange, Seasons.PriceClass>> seasonsBetween = ses.seasonsBetween( start, end );
        assertEquals( 2, seasonsBetween.size() );
        seasonsBetween = ses.seasonsBetween( start, endOfSummer );
        assertEquals( 4, seasonsBetween.size() );

//        Assert.fail( "method seasonsBetween reached end. You know what to do." );
    }
}
