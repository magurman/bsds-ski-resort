package com.bsds.client.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.bsds.client.http.UpicHttpClient;
import java.util.concurrent.ThreadLocalRandom;

public class SkierThread extends Thread {


    private final String hostname;
    private final int port;
    private final int startSkierID;
    private int endSkierID;
    private int startTime;
    private int endTime;
    private int numSkiLifts;
    private int numRuns;
    public CyclicBarrier barrier;

    public SkierThread(String hostname, int port, int startSkierID, int endSkierID, int startTime,
                       int endTime, int numSkiLifts, int numRuns, CyclicBarrier barrier) {
        this.hostname = hostname;
        this.port = port;
        this.startSkierID = startSkierID;
        this.endSkierID = endSkierID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.barrier = barrier;
        this.numSkiLifts = numSkiLifts;
        this.numRuns = numRuns;
    }

    @Override
    public void run() {
        int numSkiers = endSkierID - startSkierID + 1;
        int numRunsForPhase = numRuns * (numSkiers);
        for (int j = 1; j <= numRunsForPhase; j++) {
            int currentSkierID = ThreadLocalRandom.current().nextInt(numSkiers);
            try {
                String url = "http://" + this.hostname + ":" + this.port
                    + "/skiers/" + 10 + "/seasons/" + 10 + "/days/"
                    + 11 + "/skiers/" + currentSkierID;
                int randomTime = ThreadLocalRandom.current().nextInt(startTime,
                    endTime + 1);
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
