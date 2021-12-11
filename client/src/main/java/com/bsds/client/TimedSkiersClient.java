package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedSkiersClient {
    private int numThreads;
    private int skierIDStart;
    private int skierIDEnd;
    private int numSkiLifts;
    private final String hostname;
    private final int port;
    private static boolean done = false;
    static CyclicBarrier doneBarrier;

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

    public void start() throws InterruptedException, BrokenBarrierException {

        TimedThreadLauncher launcher = new TimedThreadLauncher(numThreads, skierIDStart, skierIDEnd, numSkiLifts, hostname, port, 1, 420,
            TimedSkiersClient.doneBarrier);
        launcher.launch();
        
        final Runnable signalDoneRunnable = new Runnable(){
            public void run(){
                TimedSkiersClient.signalDone();
            }
        };
        ScheduledExecutorService signalDoneExecutor = Executors.newScheduledThreadPool(1);
        signalDoneExecutor.schedule(signalDoneRunnable, 15, TimeUnit.MINUTES);
        doneBarrier.await();
    }

    public synchronized static void signalDone(){
        TimedSkiersClient.done = true;
    }

    public static boolean checkDone(){
        return TimedSkiersClient.done;
    }

}