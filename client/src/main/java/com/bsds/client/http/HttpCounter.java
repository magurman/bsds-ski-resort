package com.bsds.client.http;

public class HttpCounter {
    private static int succRequests = 0;
    private static int failedRequests = 0;

    public static synchronized void incrementNumSuccessful() {
        HttpCounter.succRequests++;
    }

    public static synchronized void incrementNumFailed() {
        HttpCounter.failedRequests++;
    }

    public static synchronized int getNumSuccessful() {
        return HttpCounter.succRequests;
    }

    public static synchronized int getNumFailed() {
        return HttpCounter.failedRequests;
    }

    public static synchronized void reset() {
        HttpCounter.succRequests = 0;
        HttpCounter.failedRequests = 0;
    }
}
