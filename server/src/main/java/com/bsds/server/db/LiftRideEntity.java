package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LiftRide")
public class LiftRideEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer liftRideID;

    @ManyToOne
    private LiftEntity lift;

    private Integer time;

    @ManyToOne
    private SkierEntity skier;

    private String season;

    private Integer dayID;
    
    public Integer getLiftRideID() {
        return liftRideID;
    }

    public Integer getDayID() {
        return dayID;
    }

    public void setDayID(Integer dayID) {
        this.dayID = dayID;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public SkierEntity getSkier() {
        return skier;
    }

    public void setSkier(SkierEntity skier) {
        this.skier = skier;
    }

    public void setLiftRideID(Integer liftRideID) {
        this.liftRideID = liftRideID;
    }

    public LiftEntity getLift() {
        return lift;
    }

    public void setLift(LiftEntity lift) {
        this.lift = lift;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
