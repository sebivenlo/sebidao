package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 * Map trucks.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckPlanMapper extends AbstractMapper<Integer,TruckPlan> {

    public TruckPlanMapper(  ) {
        super( Integer.class, TruckPlan.class );
    }

    @Override
    public Function<TruckPlan, Integer> keyExtractor() {
        return TruckPlan::getNaturalId;
    }
   
}
