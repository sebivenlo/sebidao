package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Truck;
import entities.TruckMapper;
import entities.TruckPlan;
import entities.TruckPlanMapper;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.pgtypes.TSRange;
import nl.fontys.sebivenlo.pgtypes.TSRange;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Make sure pg tsrange checks against double bookings.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class NoDoublePlansTest extends DBTestHelpers {

    PGDAO<Integer, Truck> tDao;
    PGDAO<Integer, TruckPlan> pDao;

    @BeforeAll
    public static void setupClass() {
        DBTestHelpers.setupClass();
        daof.registerMapper( Truck.class, new TruckMapper() );
        daof.registerMapper( TruckPlan.class, new TruckPlanMapper() );
        loadDatabase();
    }

    @BeforeEach
    public void setUp() throws Throwable {
        assertThat( daof ).isNotNull();
        tDao = daof.createDao( Truck.class );
        assertThat( tDao ).isNotNull();
        pDao = daof.createDao( TruckPlan.class );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void noOverlaps() throws Exception {

        Truck t1 = new Truck( 0, "Big Benz" );
        assertThat( tDao ).isNotNull();
        try {
            t1 = tDao.save( t1 );
        } catch ( Throwable t ) {
            t.printStackTrace();
            fail( t.getMessage() );
        }
        LocalDateTime start1 = now().plus( 1, DAYS );
        LocalDateTime end1 = now().plus( 1, DAYS ).plus( 2, HOURS );

        TSRange ts1 = new TSRange( start1, end1 );
        TSRange ts2 = new TSRange( start1, end1.minus( 20, MINUTES ) );
        TSRange ts3 = new TSRange( end1, end1.plus( 25, MINUTES ) );
        TruckPlan plan1 = new TruckPlan( t1.getId(), ts1 );
        TruckPlan plan2 = new TruckPlan( t1.getId(), ts2 );

        try ( TransactionToken tok = pDao.startTransaction() ) {

            pDao.save( plan1 );
            assertThatThrownBy( () -> {
                pDao.save( plan2 );
            } ).isExactlyInstanceOf( DAOException.class );
        }

//        fail( "method method reached end. You know what to do." );
    }

    @Test
    public void twoPlans() throws Exception {

        Truck t1 = new Truck( 0, "Big Benz" );
        t1 = tDao.save( t1 );
        LocalDateTime start1 = now().plus( 1, DAYS );
        LocalDateTime end1 = now().plus( 1, DAYS ).plus( 2, HOURS );

        TSRange ts1 = new TSRange( start1, end1 );
        TSRange ts2 = new TSRange( end1, end1
                                   .plus( 25, MINUTES ) );
        TruckPlan plan1 = new TruckPlan( t1.getId(), ts1 );
        TruckPlan plan2 = new TruckPlan( t1.getId(), ts2 );

        try ( TransactionToken tok = pDao.startTransaction() ) {

            pDao.save( plan1 );
            pDao.save( plan2 );
            tok.commit();
        }

//        Assert.fail( "method method reached end. You know what to do." );
    }
}
