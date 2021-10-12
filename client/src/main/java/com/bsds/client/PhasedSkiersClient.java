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

    System.out.println("10 percent of Phase One complete! Launching Phase Two!");
    System.out.println(String.format("Current number of threads completed: %d",
        phaseOneBarrier.getNumberWaiting()));

    int phaseTwoNumRuns = (int) Math.round(0.8 * numRuns);
    PhaseRunner phaseTwo = new PhaseRunner(numThreads, skierIDStart, skierIDEnd, numSkiLifts,
        hostname, port, 91, 360, phaseTwoNumRuns, phaseTwoBarrier);
    phaseTwo.start();

    while(phaseTwoBarrier.getNumberWaiting() < Math.round(numThreads *.1)){
    }

    System.out.println("10 percent of Phase Two complete! Launching Phase Three!");
    System.out.println(String.format("Current number of threads completed: %d",
        phaseTwoBarrier.getNumberWaiting()));

    int phaseThreeNumRuns = (int) Math.round(0.1 * numRuns);
    PhaseRunner phaseThree = new PhaseRunner(numThreads/4, skierIDStart, skierIDEnd,
        numSkiLifts, hostname, port, 361, 420, phaseThreeNumRuns,
        phaseThreeBarrier);
    phaseThree.start();

    try {
      phaseOneBarrier.await();
      System.out.println("Phase One successfully completed!");
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

    try {
      phaseTwoBarrier.await();
      System.out.println("Phase Two successfully completed!");
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

    try {
      phaseThreeBarrier.await();
      System.out.println("Phase Three successfully completed!");
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

}
