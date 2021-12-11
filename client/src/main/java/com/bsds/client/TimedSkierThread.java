package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

import com.bsds.client.TimedSkiersClient;
import com.bsds.client.http.UpicHttpClient;
import com.bsds.client.model.LiftRide;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class TimedSkierThread extends Thread {
    private final String hostname;
    private final int port;
    private int startTime;
    private int endTime;
    private int numSkiLifts;
    private int startSkierId;
    private int endSkierId;
    private CyclicBarrier doneBarrier;

    private static final Logger logger = LogManager.getLogger(UpicHttpClient.class);
    
    public TimedSkierThread(String hostname, int port, int startSkierID, int endSkierID, 
        int startTime, int endTime, int numSkiLifts, CyclicBarrier doneBarrier){
            this.hostname = hostname;
            this.port = port;
            this.startSkierId = startSkierID;
            this.endSkierId = endSkierID;
            this.startTime = startTime;
            this.endTime = endTime;
            this.numSkiLifts = numSkiLifts;
    }

    @Override
    public void run(){
        while(!(TimedSkiersClient.checkDone())){
            int currentSkierID = ThreadLocalRandom.current().nextInt(this.startSkierId, this.endSkierId + 1);
            int resortID = ThreadLocalRandom.current().nextInt(0, 5);

            int liftID = resortID * 8 + ThreadLocalRandom.current().nextInt(0,8);
            try {
                // String url = "http://" + this.hostname + ":" + this.port
                //     +  "/skiers/" + resortID + "/seasons/2021/days/"
                //     + 1 + "/skiers/" + currentSkierID;

                String url = this.buildUrl("/liftrides");
                // random time from range for phase
                int randomTime = ThreadLocalRandom.current().nextInt(startTime,
                    endTime + 1);

                long start_time = System.currentTimeMillis();
                // UpicHttpClient.postWriteLiftRide(url, randomTime, liftID); // Make POST request

                UpicHttpClient.postLiftRide(url, new LiftRide(randomTime, liftID, currentSkierID, resortID));

                LatencyHistogram.update(System.currentTimeMillis() - start_time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            doneBarrier.await();
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted.");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            logger.error("Cyclic Barrier is broken. Could not await.");
            e.printStackTrace();
        }
    }

    private String buildUrl(String path) {

        return "http://" + this.hostname + ":" + this.port + path;
    }

}