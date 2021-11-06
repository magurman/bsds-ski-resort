package com.bsds.client.threads;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.bsds.client.http.HttpCounter;
import com.bsds.client.http.UpicHttpClient;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class which is responsible for executing the workload of a single thread
 * in the client. When launched, it will submit numRunsForPhase = {phasefactor} * numSkiers
 * POST requests to the server. Each request corresponds to a single ride of a ski lift by
 * a skier. 
 */
public class SkierThread extends Thread {

    private final String hostname;
    private final int port;
    private int startTime;
    private int endTime;
    private int numSkiLifts;
    private int numSkiers;
    private int numRunsForPhase;
    private CountDownLatch triggerNextPhaseLatch;
    private CountDownLatch doneLatch;
    private int startSkierId;
    private int endSkierId;

    public SkierThread(String hostname, int port, int startSkierID, int endSkierID, int startTime,
                       int endTime, int numSkiLifts, int numRuns, CountDownLatch triggerNextPhaseLatch, CountDownLatch doneLatch) {
        this.hostname = hostname;
        this.port = port;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numSkiLifts = numSkiLifts;
        this.startSkierId = startSkierID;
        this.endSkierId = endSkierID;
        this.numSkiers = endSkierID - startSkierID + 1; //calculate number of skiers in range
        this.numRunsForPhase = numRuns * numSkiers; // total number of runs aka number of POST requests
        this.triggerNextPhaseLatch = triggerNextPhaseLatch;
        this.doneLatch = doneLatch;
    }

    @Override
    public void run() {
        // launch post requests 
        for (int j = 1; j <= numRunsForPhase; j++) {
            // get random skier from range
            int currentSkierID = ThreadLocalRandom.current().nextInt(this.startSkierId, this.endSkierId + 1);
            int resortID = ThreadLocalRandom.current().nextInt(0, 5);
            int liftID = resortID * 8 + ThreadLocalRandom.current().nextInt(0,8);
            try {
                String url = "http://" + this.hostname + ":" + this.port
                    +  "/skiers/" + resortID + "/seasons/2021/days/"
                    + 1 + "/skiers/" + currentSkierID;
                // random time from range for phase
                int randomTime = ThreadLocalRandom.current().nextInt(startTime,
                    endTime + 1);
                // random lift 
                // int randomLift = ThreadLocalRandom.current().nextInt(1,
                //     numSkiLifts + 1);
                UpicHttpClient.postWriteLiftRide(url, randomTime, liftID); // Make POST request
                UpicHttpClient.getVerticalForSkiDay(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        triggerNextPhaseLatch.countDown();
        doneLatch.countDown();
    }
}
