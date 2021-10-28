package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LiftRideEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer liftRideID;

    private Integer liftID;

    private Integer time;
    
    
    public Integer getLiftRideID() {
        return liftRideID;
    }

    public void setLiftRideID(Integer liftRideID) {
        this.liftRideID = liftRideID;
    }

    public Integer getLiftID() {
        return liftID;
    }

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    

    
}
