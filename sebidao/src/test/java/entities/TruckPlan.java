package entities;

import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;
import nl.fontys.sebivenlo.dao.pg.TsRange;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckPlan implements Entity2<Integer> {

    @ID
    private Integer truckplanid;
    private Integer truckid;
    private TsRange plan;

    public TruckPlan( Integer truckplanid, Integer truckid, TsRange plan ) {
        this.truckplanid = truckplanid;
        this.truckid = truckid;
        this.plan = plan;
    }

    
    public TruckPlan( Integer truckid, TsRange plan ) {
        this.truckid = truckid;
        this.plan = plan;
    }
    
    
    @Override
    public Integer getNaturalId() {
        return truckplanid;
    }

    @Override
    public int getId() {
        return truckplanid;
    }

    Object[] asParts() {
        return new Object[]{this.truckplanid,this.truckid,this.plan};
    }
    
}
