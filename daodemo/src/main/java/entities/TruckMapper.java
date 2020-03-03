package entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 * Map trucks.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckMapper extends AbstractMapper<Integer,Truck> {

    public TruckMapper( ) {
        super( Integer.class, Truck.class );
    }

    
    @Override
    public Function<Truck, Integer> keyExtractor() {
        return Truck::getNaturalId;
    }

    
}
