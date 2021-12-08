package com.bsds.server.model;

public class LiftRide {
    public int time;
    public int liftID;
    public int skier;
    public int resort;

    public LiftRide(int time, int liftID, int skier, int resort) {
        this.time = time;
        this.liftID = liftID;
        this.skier = skier;
        this.resort = resort;
    }
}
