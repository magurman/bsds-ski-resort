package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Lift")
public class LiftEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer liftID;

    @ManyToOne
    private ResortEntity resort;

    private Integer liftNumber;

    private Integer verticalDistance;

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    public Integer getLiftID() {
        return this.liftID;
    }

    public ResortEntity getResort() {
        return resort;
    }

    public Integer getVerticalDistance() {
        return verticalDistance;
    }

    public void setVerticalDistance(Integer verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public Integer getLiftNumber() {
        return liftNumber;
    }

    public void setLiftNumber(Integer liftNumber) {
        this.liftNumber = liftNumber;
    }

    public void setResort(ResortEntity resort) {
        this.resort = resort;
    }

}
