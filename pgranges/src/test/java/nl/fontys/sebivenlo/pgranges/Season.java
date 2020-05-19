package nl.fontys.sebivenlo.pgranges;

import java.time.LocalDate;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Season {

    String name;
    LocalDateRange dateRange;
    Seasons.PriceClass priceClas;

    LocalDate lastStay() {

        return dateRange.getEnd().minusDays( 1 );
    }

}
