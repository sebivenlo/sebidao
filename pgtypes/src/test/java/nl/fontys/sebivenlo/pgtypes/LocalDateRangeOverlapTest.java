package nl.fontys.sebivenlo.pgtypes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static nl.fontys.sebivenlo.pgtypes.LocalDateTimeRange.fromUntil;
import static nl.fontys.sebivenlo.pgtypes.LocalDateRangeTestBase.A;
import static nl.fontys.sebivenlo.pgtypes.LocalDateRangeTestBase.B;
import static nl.fontys.sebivenlo.pgtypes.LocalDateRangeTestBase.C;
import static nl.fontys.sebivenlo.pgtypes.LocalDateRangeTestBase.D;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )
public class LocalDateRangeOverlapTest extends RangeTestBase<LocalDate,Range<LocalDate>> {

    public LocalDateRangeOverlapTest( boolean expected, String label ) {
        super( expected, label );
    }
  
    @Override
    Range<LocalDate> createRange( LocalDate start, LocalDate end ) {
        return new LocalDateRange(start, end);
    }

    @Override
    LocalDate a() {
        return A;
    }

    @Override
    LocalDate b() {
        return B;
    }

    @Override
    LocalDate c() {
        return C;
    }

    @Override
    LocalDate d() {
        return D;
    }

    @Override
    Object measurementUnit() {
        return ChronoUnit.DAYS;
    }
    
    
}
