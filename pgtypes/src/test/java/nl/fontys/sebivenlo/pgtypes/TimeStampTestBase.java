package nl.fontys.sebivenlo.pgtypes;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TimeStampTestBase {
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

    static final LocalDateTime A = now( clock ).minus( 2, HOURS );
    static final LocalDateTime B = A.plus( 30, MINUTES );
    static final LocalDateTime C = B.plus( 10, MINUTES );
    static final LocalDateTime D = C.plus( 1, HOURS );
}
