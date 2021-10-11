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


  public PhaseRunner(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts,
      String hostname, int port, int startTime, int endTime, CyclicBarrier barrier) {
    this.numThreads = numThreads;
    this.skierIDStart = skierIDStart;
    this.skierIDEnd = skierIDEnd;
    this.numSkiLifts = numSkiLifts;
    this.hostname = hostname;
    this.port = port;
    this.startTime = startTime;
    this.endTime = endTime;
    this.barrier = barrier;
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
      new SkierThread(hostname, port, threadIDStart, threadIDEnd, startTime, endTime,
          123409123, barrier).start();
    }
  }

}
