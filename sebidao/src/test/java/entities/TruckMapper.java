package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 * Map trucks.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckMapper extends AbstractMapper<Integer,Truck> {

    public TruckMapper( Class<Integer> keyType, Class<Truck> entityType ) {
        super( keyType, entityType );
    }

    
    @Override
    public Object[] explode( Truck t ) {
        return t.asParts();
    }

    @Override
    public Function<Truck, Integer> keyExtractor() {
        return Truck::getNaturalId;
    }
    
}
