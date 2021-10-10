package com.bsds.client.threads;

import com.bsds.client.HttpClient;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;


public class SkierThread extends Thread {
    private final String hostname;
    private final int port;
    private final int startSkierID;
    private int endSkierID;
    private int startTime;
    private int endTime;
    private float nextPhaseTrigger;
    public CyclicBarrier barrier;
    private final HttpClient client;

    // Envisioned this as the individual thread that will be responsible for running a certain number of POST requests.
    // TODO: Find a way to remove the first for loop. Test and make sure it can send requests

    public SkierThread(String hostname, int port, int startSkierID, int endSkierID, int startTime,
                       int endTime, float nextPhaseTrigger, CyclicBarrier barrier) {

        this.hostname = hostname;
        this.port = port;
        this.startSkierID = startSkierID;
        this.endSkierID = endSkierID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nextPhaseTrigger = nextPhaseTrigger; // This is important as we have to .await() the cyclic barrier when we reach this point.
        this.barrier = barrier;
        this.client = new HttpClient(); // See class ApiClient. Makes the post request
    }

    @Override
    public void run() {

        // Empty array of ints, to hold the entire range of skierIDs passed to this thread.
        ArrayList<Integer> skierIDs = new ArrayList<>();
        for (int i = startSkierID; i <= endSkierID; i++) {
            skierIDs.add(i);
        }
        // For loop to go through and make a POST request for each skierID in the range.
        for (int j = startSkierID; j <= endSkierID; j++) {
            int currentSkierID = getRandomSkierID(skierIDs);
            skierIDs.remove(skierIDs.indexOf(currentSkierID));
            try {
                this.client.postWriteLifeRide(12, 24, 1, currentSkierID); // Make POST request
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Responsible for getting a random skierID by generating a random index.
     * @param skierIDs array of all skierIDs
     * @return a specific skierID, generated randomly
     */
    private int getRandomSkierID(ArrayList<Integer> skierIDs) {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(skierIDs.size());
        return skierIDs.get(randomIndex);
    }
}
