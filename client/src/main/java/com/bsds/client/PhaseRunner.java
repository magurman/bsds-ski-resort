package com.bsds.client;

import com.bsds.client.threads.SkierThread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * A class to execute a phase in PhasedSkiersClient
 */
public class PhaseRunner extends Thread  {

  private int numThreads;
  private int numSkiLifts;
  private String hostname;
  private int port;
  private int startTime;
  private int endTime;
  private int numRuns;
  private int numSkiers;
  private CountDownLatch triggerNextPhaseLatch;
  private CountDownLatch doneLatch;

  public PhaseRunner(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts,
      String hostname, int port, int startTime, int endTime, int numRuns, CountDownLatch triggerNextPhasLatch,
      CountDownLatch doneLatch) {
    this.numThreads = numThreads;
    this.numSkiLifts = numSkiLifts;
    this.hostname = hostname;
    this.port = port;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numRuns = numRuns;
    this.numSkiers = skierIDEnd - skierIDStart + 1;
    this.triggerNextPhaseLatch = triggerNextPhasLatch;
    this.doneLatch = doneLatch;
  }

  /**
   * Run this phase. 
   * 
   * Creates numThreads SkierThreads to execute Post requests, divides the
   * range of skiers evenly among the threads that it launches 
   */
  @Override
  public void run() {
    for (int i = 0; i < numThreads; i++) {
      int threadIDStart = i * (numSkiers / numThreads) + 1;
      int threadIDEnd = (i + 1) * (numSkiers / numThreads);
      new SkierThread(hostname, port, threadIDStart, threadIDEnd, startTime, endTime, numSkiLifts,
        numRuns, triggerNextPhaseLatch, doneLatch).start();
    }
  }

}
