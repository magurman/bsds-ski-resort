package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * A class to manage multiple phases of sending client requests to the Upic Ski Resort Server.
 * 
 * This class currently launches three phases of sending requests to the server: startup, peak, and cooldown
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

    // launch phase one - startup
    int phaseOneNumRuns = (int) Math.round(0.1 * numRuns);
    PhaseRunner phaseOne = new PhaseRunner(numThreads/4, skierIDStart, skierIDEnd,
        numSkiLifts, hostname, port, 1, 90, phaseOneNumRuns,
        PhasedSkiersClient.phaseOneBarrier);
    phaseOne.start();

    // wait for phase one to be 10% complete before continuing
    while(phaseOneBarrier.getNumberWaiting() < Math.round((float) (numThreads/4)*.1)){
    }

    // launch phase 2 - peak
    int phaseTwoNumRuns = (int) Math.round(0.8 * numRuns);
    PhaseRunner phaseTwo = new PhaseRunner(numThreads, skierIDStart, skierIDEnd, numSkiLifts,
        hostname, port, 91, 360, phaseTwoNumRuns, phaseTwoBarrier);
    phaseTwo.start();

    // wait for phase two to be 10% complete before continuing 
    while(phaseTwoBarrier.getNumberWaiting() < Math.round(numThreads *.1)){
    }

    // launch phase 3
    int phaseThreeNumRuns = (int) Math.round(0.1 * numRuns);
    PhaseRunner phaseThree = new PhaseRunner(numThreads/4, skierIDStart, skierIDEnd,
        numSkiLifts, hostname, port, 361, 420, phaseThreeNumRuns,
        phaseThreeBarrier);
    phaseThree.start();

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

}
