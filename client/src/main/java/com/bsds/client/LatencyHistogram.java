package com.bsds.client;

public class LatencyHistogram {
    static long[] histogram = new long[500];
    static int over_5s = 0;
    

    public static synchronized void update(long latency){
        if(latency >= 5000){
            over_5s++;
        } else {
            histogram[(int) latency/10]++;
        }
    }

    public static long[] readHistogram(){
        return histogram.clone();
    }
}