package nl.fontys.sebivenlo.pgtypes;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.WEEKS;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateRangeTestBase {
    static Clock clock = new Clock() {
        @Override
        public ZoneId getZone() {
            return systemDefault();
        }

        @Override
        public Clock withZone( ZoneId zone ) {

            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochSecond( 1557223200L );
        }
    };

    static final LocalDate A = now( clock ).minus( 2, WEEKS );
    static final LocalDate B = A.plus( 3, DAYS );
    static final LocalDate C = B.plus( 10, DAYS );
    static final LocalDate D = C.plus( 1, WEEKS );
}
