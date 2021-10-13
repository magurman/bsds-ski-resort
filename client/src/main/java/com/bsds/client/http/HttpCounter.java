package com.bsds.client.http;

/**
 * Counter class for number of successful and failed HttpRequests.
 * 
 * For use in UpicHttpCounter
 */
public class HttpCounter {
    private static int succRequests = 0;
    private static int failedRequests = 0;

    /**
     * Increment counter for successful requests
     */
    public static synchronized void incrementNumSuccessful() {
        HttpCounter.succRequests++;
    }

    /**
     * Increment counter for failed requests
     */
    public static synchronized void incrementNumFailed() {
        HttpCounter.failedRequests++;
    }

    /**
     * Get value for number of successful requests 
     */
    public static synchronized int getNumSuccessful() {
        return HttpCounter.succRequests;
    }

    /**
     * Get value for number of failed requests 
     */
    public static synchronized int getNumFailed() {
        return HttpCounter.failedRequests;
    }

    /**
     * Reset counters for successful and faield requests
     */
    public static synchronized void reset() {
        HttpCounter.succRequests = 0;
        HttpCounter.failedRequests = 0;
    }
}
