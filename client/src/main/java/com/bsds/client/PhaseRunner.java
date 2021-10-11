package com.bsds.client;

import com.bsds.client.threads.SkierThread;
import java.util.concurrent.CyclicBarrier;

public class PhaseRunner extends Thread  {

  private int numThreads;
  private int skierIDStart;
  private int skierIDEnd;
  private int numSkiLifts;
  private String hostname;
  private int port;
  private int startTime;
  private int endTime;
  private CyclicBarrier barrier;
  private int numRuns;


  public PhaseRunner(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts,
      String hostname, int port, int startTime, int endTime, int numRuns, CyclicBarrier barrier) {
    this.numThreads = numThreads;
    this.skierIDStart = skierIDStart;
    this.skierIDEnd = skierIDEnd;
    this.numSkiLifts = numSkiLifts;
    this.hostname = hostname;
    this.port = port;
    this.startTime = startTime;
    this.endTime = endTime;
    this.barrier = barrier;
    this.numRuns = numRuns;
  }

  @Override
  public void run() {
    int numSkiers = skierIDEnd - skierIDStart + 1;
    for (int i = 0; i < numThreads; i++) {
      int threadIDStart = i * (numSkiers / numThreads) + 1;
      int threadIDEnd = (i + 1) * (numSkiers / numThreads);
      System.out.println(String.format("num skiers: %d", numSkiers));
      System.out.println("Launching thread with following starting and ending Skier IDs");
      System.out.println(String.format("Start ID: %d", threadIDStart));
      System.out.println(String.format("End ID: %d", threadIDEnd));
      System.out.println(i);
      new SkierThread(hostname, port, threadIDStart, threadIDEnd, startTime, endTime,
          numSkiLifts, numRuns, barrier).start();
    }
  }

}
