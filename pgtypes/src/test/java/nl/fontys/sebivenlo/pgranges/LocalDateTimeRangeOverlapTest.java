package nl.fontys.sebivenlo.pgranges;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static nl.fontys.sebivenlo.pgranges.LocalDateTimeRangeTestBase.A;
import static nl.fontys.sebivenlo.pgranges.LocalDateTimeRangeTestBase.B;
import static nl.fontys.sebivenlo.pgranges.LocalDateTimeRangeTestBase.C;
import static nl.fontys.sebivenlo.pgranges.LocalDateTimeRangeTestBase.D;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )
public class LocalDateTimeRangeOverlapTest extends RangeTestBase<LocalDateTime,Range<LocalDateTime>> {

    public LocalDateTimeRangeOverlapTest( boolean expected, String label ) {
        super( expected, label );
    }
  
    @Override
    Range<LocalDateTime> createRange( LocalDateTime start, LocalDateTime end ) {
        return new LocalDateTimeRange(start, end);
    }

    @Override
    LocalDateTime a() {
        return A;
    }

    @Override
    LocalDateTime b() {
        return B;
    }

    @Override
    LocalDateTime c() {
        return C;
    }

    @Override
    LocalDateTime d() {
        return D;
    }

    @Override
    Object measurementUnit() {
        return ChronoUnit.MICROS;
    }
}
