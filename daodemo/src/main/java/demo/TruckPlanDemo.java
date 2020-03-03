package demo;

import entities.PGDataSource;
import entities.Truck;
import entities.TruckMapper;
import entities.TruckPlan;
import entities.TruckPlanMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.pg.PGDAO;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import nl.fontys.sebivenlo.pgtypes.TSRange;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TruckPlanDemo {

    public static void main( String[] args ) throws Exception {
        new TruckPlanDemo().planSuccessfullyDemo();
        new TruckPlanDemo().planFailDemo();
        
        
        System.out.println( "=============" );
        PGDAO<Integer, TruckPlan> dao2 = pdaof.createDao( TruckPlan.class );
        Collection<TruckPlan> all = dao2.getAll();
        all.forEach( System.out::println );
        dao2.deleteAll( all ); //clean up after demo.
        dao2.close();

    }

    private void planSuccessfullyDemo() {
        final DAO<Integer, TruckPlan> tpDao = pdaof.createDao( TruckPlan.class );
        final DAO<Integer, Truck> truckDao = pdaof.createDao( Truck.class );
        Collection<Truck> byColumnValues = truckDao
                .getByColumnValues( "plate", "Vroom" );
        System.out.println( "byColumnValues = " + byColumnValues );
        Truck vroom = byColumnValues.stream().findFirst().get();

        try (
                TransactionToken ttok = tpDao.startTransaction(); ) {
            TruckPlan tp1 = new TruckPlan( vroom.getTruckid(), new TSRange( LocalDateTime
                                           .now(), Duration.ofHours( 3 ) ) );
            TruckPlan tp2 = new TruckPlan( vroom.getTruckid(), new TSRange( LocalDateTime
                                           .now().plusHours( 4 ), Duration
                                                                            .ofHours( 3 ) ) );
            tpDao.saveAll( tp1, tp2 );

            Collection<TruckPlan> all = tpDao.getAll();
            all.forEach( System.out::println );
            ttok.commit(); 
        } catch ( Exception ex ) {
            Logger.getLogger( TruckPlanDemo.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }

    }

    private void planFailDemo() {
        DAO<Integer, TruckPlan> tpDao = pdaof.createDao( TruckPlan.class );
        final DAO<Integer, Truck> truckDao = pdaof.createDao( Truck.class );
        Collection<Truck> byColumnValues = truckDao
                .getByColumnValues( "plate", "Volvo" );
        System.out.println( "byColumnValues = " + byColumnValues );
        Truck vroom = byColumnValues.stream().findFirst().get();

        try (
                TransactionToken ttok = tpDao.startTransaction(); ) {
            TruckPlan tp1 = new TruckPlan( vroom.getTruckid(), new TSRange( LocalDateTime
                                           .now(), Duration.ofHours( 3 ) ) );
            TruckPlan tp2 = new TruckPlan( vroom.getTruckid(), new TSRange( LocalDateTime
                                           .now().plusHours( 1 ), Duration
                                                                            .ofHours( 3 ) ) );
            tpDao.saveAll( tp1, tp2 );
            ttok.commit(); // should not succeed.
        } catch ( Exception expected ) {

            Logger.getLogger( TruckPlanDemo.class.getName() )
                    .log( Level.SEVERE, null, expected );
        }

    }

    private static final PGDAOFactory pdaof = new PGDAOFactory( PGDataSource.DATA_SOURCE );

    static {

        pdaof.registerMapper( Truck.class, new TruckMapper() );
        pdaof.registerMapper( TruckPlan.class, new TruckPlanMapper() );
    }
}
