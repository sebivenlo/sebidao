package entities;

import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Truck implements Entity2<Integer>  {

    @ID
    private Integer truckid;
    private String plate;

    public Truck( Integer truckid, String Plate ) {
        this.truckid = truckid;
        this.plate = Plate;
    }
    
    
    @Override
    public Integer getNaturalId() {
        return truckid;
    }

    @Override
    public int getId() {
        return truckid;
   }

    public Integer getTruckid() {
        return truckid;
    }

    public void setTruckid( Integer truckid ) {
        this.truckid = truckid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate( String Plate ) {
        this.plate = Plate;
    }
    
    Object[] asParts(){
        return new Object[]{
            this.truckid,
            this.plate
        };
    }
}
