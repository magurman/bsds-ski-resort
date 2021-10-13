package com.bsds.client;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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

    int phaseOneNumRuns = (int) Math.round(0.1 * numRuns);
    PhaseRunner phaseOne = new PhaseRunner(numThreads/4, skierIDStart, skierIDEnd,
        numSkiLifts, hostname, port, 1, 90, phaseOneNumRuns,
        PhasedSkiersClient.phaseOneBarrier);
    phaseOne.start();

    while(phaseOneBarrier.getNumberWaiting() < Math.round((float) (numThreads/4)*.1)){
    }


    int phaseTwoNumRuns = (int) Math.round(0.8 * numRuns);
    PhaseRunner phaseTwo = new PhaseRunner(numThreads, skierIDStart, skierIDEnd, numSkiLifts,
        hostname, port, 91, 360, phaseTwoNumRuns, phaseTwoBarrier);
    phaseTwo.start();

    while(phaseTwoBarrier.getNumberWaiting() < Math.round(numThreads *.1)){
    }

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
