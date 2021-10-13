package com.bsds.client;

import com.bsds.client.threads.SkierThread;
import java.util.concurrent.CyclicBarrier;

public class PhaseRunner extends Thread  {

  private int numThreads;
  private int numSkiLifts;
  private String hostname;
  private int port;
  private int startTime;
  private int endTime;
  private CyclicBarrier barrier;
  private int numRuns;
  private int numSkiers;


  public PhaseRunner(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts,
      String hostname, int port, int startTime, int endTime, int numRuns, CyclicBarrier barrier) {
    this.numThreads = numThreads;
    this.numSkiLifts = numSkiLifts;
    this.hostname = hostname;
    this.port = port;
    this.startTime = startTime;
    this.endTime = endTime;
    this.barrier = barrier;
    this.numRuns = numRuns;
    this.numSkiers = skierIDEnd - skierIDStart + 1;
  }

  @Override
  public void run() {
    for (int i = 0; i < numThreads; i++) {
      int threadIDStart = i * (numSkiers / numThreads) + 1;
      int threadIDEnd = (i + 1) * (numSkiers / numThreads);
      new SkierThread(hostname, port, threadIDStart, threadIDEnd, startTime, endTime,
          numSkiLifts, numRuns, barrier).start();
    }
  }

}
