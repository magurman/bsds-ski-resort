package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * A class to manage multiple phases of sending client requests to the Upic Ski Resort Server.
 * 
 * This class currently launches three phases of sending requests to the server: startup, peak, and cooldown
 * 
 */
public class PhasedSkiersClient {
  private int numThreads;
  private int skierIDStart;
  private int skierIDEnd;
  private int numSkiLifts;
  private final String hostname;
  private final int port;
  static CyclicBarrier phaseOneBarrier;
  static CyclicBarrier phaseTwoBarrier;
  static CyclicBarrier phaseThreeBarrier;
  private int numRuns;

  public PhasedSkiersClient(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts,
      int numRuns, String hostname, int port){
    this.numThreads = numThreads;
    this.skierIDStart = skierIDStart;
    this.skierIDEnd = skierIDEnd;
    this.numSkiLifts = numSkiLifts;
    this.hostname = hostname;
    this.port = port;
    this.numRuns = numRuns;
    PhasedSkiersClient.phaseOneBarrier = new CyclicBarrier((numThreads/4)+1);
    PhasedSkiersClient.phaseTwoBarrier = new CyclicBarrier(numThreads + 1);
    PhasedSkiersClient.phaseThreeBarrier = new CyclicBarrier((numThreads/4)+1);
  }

  public void start() throws InterruptedException {

    // launch phase 1 10% of runs/skier, 1/4 of total threads, start at min 1 end at min 90
    launchPhase((float) 0.1, 4, 1, 90, PhasedSkiersClient.phaseOneBarrier);

    // wait for phase one to be 10% complete before continuing
    while(phaseOneBarrier.getNumberWaiting() < Math.round((float) (numThreads/4)*.1)){
    }

    // launch phase 2 80% of runs/skier, max num threads, start at min 91 end at min 360
    launchPhase((float) 0.8, 1, 91, 360, PhasedSkiersClient.phaseTwoBarrier);

    // wait for phase two to be 10% complete before continuing 
    while(phaseTwoBarrier.getNumberWaiting() < Math.round(numThreads *.1)){
    }

    // launch phase 3 10% of runs, 1/4 num threads, start at min 361 end at min 420
    launchPhase((float) 0.1, 4, 361, 420, PhasedSkiersClient.phaseThreeBarrier);

    try {
      phaseOneBarrier.await();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

    try {
      phaseTwoBarrier.await();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

    try {
      phaseThreeBarrier.await();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

  private void launchPhase(float runScaleFactor, int threadScaleFactor, int startTime,
   int endTime, CyclicBarrier barrier){
    // launch phase one - startup
    int phaseNumRuns = (int) Math.round(runScaleFactor * numRuns);
    int phaseNumThreads = numThreads/threadScaleFactor;
    PhaseRunner phase = new PhaseRunner(phaseNumThreads, skierIDStart, skierIDEnd,
        numSkiLifts, hostname, port, startTime, endTime, phaseNumRuns,
        barrier);
    phase.start();
  }

}
