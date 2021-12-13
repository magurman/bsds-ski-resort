package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TimedSkiersClient implements SkiersClient{
    private int numThreads;
    private int skierIDStart;
    private int skierIDEnd;
    private int numSkiLifts;
    private final String hostname;
    private final int port;
    private static boolean done = false;
    static CyclicBarrier doneBarrier;

    private static final Logger logger = LogManager.getLogger(TimedSkiersClient.class);

    public TimedSkiersClient(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts, String hostname,
            int port) {
        this.numThreads = numThreads;
        this.skierIDStart = skierIDStart;
        this.skierIDEnd = skierIDEnd;
        this.numSkiLifts = numSkiLifts;
        this.hostname = hostname;
        this.port = port;
        TimedSkiersClient.doneBarrier = new CyclicBarrier(numThreads + 1);
    }

    public void start() throws InterruptedException {

        TimedThreadLauncher launcher = new TimedThreadLauncher(numThreads, skierIDStart, skierIDEnd, numSkiLifts, hostname, port, 1, 420,
            TimedSkiersClient.doneBarrier);
        launcher.launch();
        
        final Runnable signalDoneRunnable = new Runnable(){
            public void run(){
                TimedSkiersClient.signalDone();
            }
        };


        final Runnable updateThroughputRunnable = new Runnable(){
            public void run(){
                ThroughputStatistics.updateThroughput();
            }
        };

        ScheduledExecutorService signalDoneExecutor = Executors.newScheduledThreadPool(1);
        signalDoneExecutor.schedule(signalDoneRunnable, 20, TimeUnit.SECONDS);

        ScheduledExecutorService updateThroughputExecutor = Executors.newScheduledThreadPool(1);
        ScheduledFuture futures = updateThroughputExecutor.scheduleAtFixedRate(updateThroughputRunnable, 0, 5, TimeUnit.SECONDS);
        try {
            doneBarrier.await();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            logger.error("Cyclic Barrier is broken. Could not await.");
            e.printStackTrace();
        }
        futures.cancel(true);
    }

    public synchronized static void signalDone(){
        TimedSkiersClient.done = true;
    }

    public static boolean checkDone(){
        return TimedSkiersClient.done;
    }

}