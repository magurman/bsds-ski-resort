package com.bsds.client;
import java.util.ArrayList;

public class ThroughputStatistics {

  private static ArrayList<Integer> throughputs = new ArrayList<>();

  private static int numReq = 0;

  public static synchronized void incrementRequest() {
    numReq++;
  }

  public static synchronized void updateThroughput() {
    throughputs.add(numReq);
    numReq = 0;
  }

  public static ArrayList<Integer> getStats() {
    return throughputs;
  }
  
}
