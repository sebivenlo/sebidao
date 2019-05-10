package entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;
import nl.fontys.sebivenlo.dao.pg.TsRange;

/**
 * Map trucks.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckPlanMapper extends AbstractMapper<Integer,TruckPlan> {

    public TruckPlanMapper( Class<Integer> keyType, Class<TruckPlan> entityType ) {
        super( keyType, entityType );
    }

    
    @Override
    public Object[] explode( TruckPlan tp ) {
        return tp.asParts();
    }

    @Override
    public Function<TruckPlan, Integer> keyExtractor() {
        return TruckPlan::getNaturalId;
    }

    @Override
    public TruckPlan implode( Object[] parts ) {
        Integer i1=(Integer)parts[0];
        Integer i2=(Integer)parts[1];
        TsRange ts = (TsRange) parts[2];
        return new TruckPlan( i1,i2,ts ); 
    }

    
}
