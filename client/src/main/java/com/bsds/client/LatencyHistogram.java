package com.bsds.client;

public class LatencyHistogram {
    static long[] histogramPost = new long[500];
    static int over_5s_Post = 0;
    static int numRequestsPost = 0;
    static long meanPost = 0;

    static long[] histogramGet = new long[500];
    static int over_5s_Get = 0;
    static int mumRequestsGet = 0;
    static long meanGet = 0;

    public static synchronized void updatePost(long latency){
        if(latency >= 5000){
            over_5s_Post++;
        } else {
            histogramPost[(int) latency/10]++;
        }

        long totalLatency = meanPost * numRequestsPost;
        meanPost = (totalLatency + latency) / (++numRequestsPost);
    }

    public static long[] readPostHistogram(){
        return histogramPost.clone();
    }

    public static long getMeanPost() {
        return meanPost;
    }

    public static synchronized void updateGet(long latency){
        if(latency >= 5000){
            over_5s_Get++;
        } else {
            histogramGet[(int) latency/10]++;
        }

        long totalLatency = meanGet * mumRequestsGet;
        meanGet = (totalLatency + latency) / (++mumRequestsGet);
    }

    public static long[] readGetHistogram(){
        return histogramGet.clone();
    }

    public static long getMeanGet() {
        return meanGet;
    }
}