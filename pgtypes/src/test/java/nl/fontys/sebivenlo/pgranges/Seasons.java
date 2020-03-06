package nl.fontys.sebivenlo.pgranges;

import java.time.LocalDate;
import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import java.time.Month;
import static java.time.Month.*;
import java.time.Period;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import static nl.fontys.sebivenlo.pgranges.Seasons.PriceClass.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Seasons {

    enum PriceClass {
        LOW, MID, HIGH;
    }

    Map<LocalDateRange, PriceClass> seasonClassMap
            = new LinkedHashMap<>();

    final static int THIS_YEAR = LocalDate.now().getYear();
    static LocalDate janFirst = of( THIS_YEAR, 1, 1 );

    public Seasons() {
        int tYear = LocalDate.now().getYear();
        LocalDate s1Start = of( tYear, JANUARY, 1 );
        LocalDate s2Start = of( tYear, FEBRUARY, 15 );
        LocalDate s3Start = of( tYear, MARCH, 1 );
        LocalDate s4Start = of( tYear, MAY, 1 );
        LocalDate s5Start = of( tYear, MAY, 15 );
        LocalDate s6Start = of( tYear, JUNE, 15 );
        LocalDate s7Start = of( tYear, AUGUST, 28 );
        LocalDate s8Start = of( tYear, SEPTEMBER, 28 );
        LocalDate s9Start = of( tYear, OCTOBER, 5 );
        LocalDate s10Start = of( tYear, OCTOBER, 12 );
        LocalDate s11Start = of( tYear, DECEMBER, 31 );

        seasonClassMap.put( new LocalDateRange( s1Start, s2Start ), LOW );
        seasonClassMap.put( new LocalDateRange( s2Start, s3Start ), MID );
        seasonClassMap.put( new LocalDateRange( s3Start, s4Start ), LOW );
        seasonClassMap.put( new LocalDateRange( s4Start, s5Start ), MID );
        seasonClassMap.put( new LocalDateRange( s5Start, s6Start ), HIGH );
        seasonClassMap.put( new LocalDateRange( s6Start, s7Start ), MID );
        seasonClassMap.put( new LocalDateRange( s7Start, s8Start ), HIGH );
        seasonClassMap.put( new LocalDateRange( s8Start, s9Start ), MID );
        seasonClassMap.put( new LocalDateRange( s9Start, s10Start ), HIGH );
        seasonClassMap.put( new LocalDateRange( s10Start, s11Start ), HIGH );
        seasonPrices.put( LOW, 100 );
        seasonPrices.put( MID, 140 );
        seasonPrices.put( HIGH, 180 );

    }

    PriceClass getPriceClass( LocalDate someDate ) {
        Optional<Map.Entry<LocalDateRange, PriceClass>> findFirst = findDateRange( someDate );
        if ( findFirst.isPresent() ) {
            return findFirst.get().getValue();
        } else {
            return null;
        }
    }

    int getDayPrice( PriceClass pc ) {
        return seasonPrices.get( pc );
    }

    Optional<Map.Entry<LocalDateRange, PriceClass>> findDateRange( LocalDate someDate ) {
        Optional<Map.Entry<LocalDateRange, PriceClass>> findFirst = seasonClassMap.entrySet().stream()
                .filter( e -> e.getKey().contains( someDate ) )
                .findFirst();
        return findFirst;
    }

    List<Map.Entry<LocalDateRange, PriceClass>> seasonsBetween( LocalDate begin, LocalDate end ) {
        return seasonClassMap.entrySet()
                .stream()
                .filter( e -> e.getKey().getStart().isAfter( begin ) )
                .filter( g -> g.getKey().isBefore( end ) )
                .collect( toList() );
    }

    EnumMap<PriceClass, Integer> seasonPrices = new EnumMap<>( PriceClass.class );

    long getPrice( LocalDateRange crocus ) {

        LocalDate start = crocus.getStart();
        LocalDate end = crocus.getEnd();
        Optional<Map.Entry<LocalDateRange, PriceClass>> beginSeasonInfo = findDateRange( start );
        Optional<Map.Entry<LocalDateRange, PriceClass>> endSeasonInfo = findDateRange( end );

        if ( beginSeasonInfo.isPresent() && endSeasonInfo.isPresent() ) {
            Map.Entry<LocalDateRange, PriceClass> beginSeason = beginSeasonInfo.get();
            Map.Entry<LocalDateRange, PriceClass> endSeason = endSeasonInfo.get();
            Integer dayPrice1 = seasonPrices.get( beginSeason.getValue() );
            Integer dayPrice2 = seasonPrices.get( endSeason.getValue() );
            if ( beginSeasonInfo.get().getKey() == endSeasonInfo.get().getKey() ) {
                return crocus.getLength(DAYS) * dayPrice1;
            } else {
                long firstPartDays = DAYS.between( crocus.getStart(), beginSeasonInfo.get().getKey().getEnd() ) * dayPrice1;
                long midPart = 0L;
                if ( !beginSeason.getKey().getEnd().equals( endSeason.getKey().getStart() ) ) {
                    // there are season(s) in between
                    midPart = seasonsBetween( start, end )
                            .stream()
                            .peek(e ->{ System.out.println( "e="+e+ " l ="+e.getKey().getDays() );})
                            .mapToLong( e -> e.getKey().getDays() * getDayPrice( e.getValue() ))
                            .sum();
                }
                long lastDayPrice = DAYS.between( endSeason.getKey().getStart(), crocus.getEnd() ) * dayPrice2;
                return firstPartDays + midPart + lastDayPrice;
            }
        } else {
            throw new IllegalArgumentException( "Sorry, the range is not in my scope" );
        }
    }

}
