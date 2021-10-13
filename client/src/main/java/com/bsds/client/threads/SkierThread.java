package com.bsds.client.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
    public CyclicBarrier barrier;
    private int numSkiers;
    private int numRunsForPhase;

    /**
     * Constructor, accepts the host and port for the server, the start and end IDs for the skier
     * range for which it is responsible, the startTime and endTime for the phase, the number of
     * ski lifts, the number of runs that skiers do in this phase, and a barrier object used for 
     * concurrency 
     * 
     * @param hostname
     * @param port
     * @param startSkierID
     * @param endSkierID
     * @param startTime
     * @param endTime
     * @param numSkiLifts
     * @param numRuns
     * @param barrier
     */
    public SkierThread(String hostname, int port, int startSkierID, int endSkierID, int startTime,
                       int endTime, int numSkiLifts, int numRuns, CyclicBarrier barrier) {
        this.hostname = hostname;
        this.port = port;
        this.startTime = startTime;
        this.endTime = endTime;
        this.barrier = barrier;
        this.numSkiLifts = numSkiLifts;
        this.numSkiers = endSkierID - startSkierID + 1; //calculate number of skiers in range
        this.numRunsForPhase = numRuns * numSkiers; // total number of runs aka number of POST requests
    }

    @Override
    public void run() {
        // launch post requests 
        for (int j = 1; j <= numRunsForPhase; j++) {
            // get random skier from range
            int currentSkierID = ThreadLocalRandom.current().nextInt(numSkiers);
            try {
                String url = "http://" + this.hostname + ":" + this.port
                    + "/skiers/" + 10 + "/seasons/" + 10 + "/days/"
                    + 11 + "/skiers/" + currentSkierID;
                // random time from range for phase
                int randomTime = ThreadLocalRandom.current().nextInt(startTime,
                    endTime + 1);
                // random lift 
                int randomLift = ThreadLocalRandom.current().nextInt(1,
                    numSkiLifts + 1);
                UpicHttpClient.postWriteLifeRide(url, randomTime, randomLift); // Make POST request
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
