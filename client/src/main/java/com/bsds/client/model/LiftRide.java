package com.bsds.client.model;

/**
 * POJO for a LiftRide at Upic Ski Resort
 */
public class LiftRide {
    public int time;
    public int lift;
    public int skier;
    public int resort;

    public LiftRide(int time, int lift, int skier, int resort) {
        this.time = time;
        this.lift = lift;
        this.skier = skier;
        this.resort = resort;
    }
}
