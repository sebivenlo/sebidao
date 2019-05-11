package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Employee;
import entities.Truck;
import entities.TruckMapper;
import entities.TruckPlan;
import entities.TruckPlanMapper;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * Make sure pg tsrange checks against double bookings.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class NoDoublePlansTest extends DBTestHelpers {

    PGDAO<Integer, Truck> tDao;
    PGDAO<Integer, TruckPlan> pDao;

    @BeforeClass
    public static void setupClass() {
        DBTestHelpers.setupClass();
        daof.registerPGdataType( "tsrange", TsRange.class );
        daof.registerMapper( Truck.class, new TruckMapper() );
        daof.registerMapper( TruckPlan.class, new TruckPlanMapper() );
        loadDatabase();
    }

    @Before
    public void setUp() throws Throwable {
        assertNotNull( daof );
        tDao = daof.createDao( Truck.class );
        pDao = daof.createDao( TruckPlan.class );
    }

//    @Ignore( "Think TDD" )
    @Test( expected = DAOException.class )
    public void noOverlaps() throws Exception {

        Truck t1 = new Truck( 0, "Big Benz" );
        t1 = tDao.save( t1 );
        LocalDateTime start1 = now().plus( 1, DAYS );
        LocalDateTime end1 = now().plus( 1, DAYS ).plus( 2, HOURS );

        TsRange ts1 = new TsRange( start1, end1 );
        TsRange ts2 = new TsRange( start1, end1.minus( 20, MINUTES ) );
        TsRange ts3 = new TsRange( end1, end1.plus( 25, MINUTES ) );
        TruckPlan plan1 = new TruckPlan( t1.getId(), ts1 );
        TruckPlan plan2 = new TruckPlan( t1.getId(), ts2 );

        try ( TransactionToken tok = pDao.startTransaction() ) {

            pDao.save( plan1 );
            pDao.save( plan2 );
            fail( "should not be reached" );
        } 

        Assert.fail( "method method reached end. You know what to do." );
    }

    @Test
    public void twoPlans() throws Exception {

        Truck t1 = new Truck( 0, "Big Benz" );
        t1 = tDao.save( t1 );
        LocalDateTime start1 = now().plus( 1, DAYS );
        LocalDateTime end1 = now().plus( 1, DAYS ).plus( 2, HOURS );

        TsRange ts1 = new TsRange( start1, end1 );
        TsRange ts2 = new TsRange( end1, end1.plus( 25, MINUTES ) );
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
