package entities;

import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;
import nl.fontys.sebivenlo.pgtypes.TSRange;
import nl.fontys.sebivenlo.pgtypes.TSRange;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TruckPlan implements Entity2<Integer> {

    @ID
    private Integer truckplanid;
    private Integer truckid;
    private TSRange plan;

    public TruckPlan( Integer truckplanid, Integer truckid, TSRange plan ) {
        this.truckplanid = truckplanid;
        this.truckid = truckid;
        this.plan = plan;
    }

    public TruckPlan( Integer truckid, TSRange plan ) {
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
        return new Object[] { 
            truckplanid, 
            truckid, 
            plan 
        };
    }

    public Integer getTruckplanid() {
        return truckplanid;
    }

    public Integer getTruckid() {
        return truckid;
    }

    public TSRange getPlan() {
        return plan;
    }

    @Override
    public String toString() {
        return "TruckPlan{" + "truckplanid=" + truckplanid + ", truckid=" + truckid + ", plan=" + plan + '}';
    }

    
    
}
