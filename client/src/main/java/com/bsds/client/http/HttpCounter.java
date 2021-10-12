package com.bsds.client.http;

public class HttpCounter {

    public static HttpCounter httpCounterInstance;

    private int succRequests;
    private int failedRequests;

    private HttpCounter() {
        this.succRequests = 0;
        this.failedRequests = 0;
    }

    public synchronized void succ() {
        this.succRequests++;
    }

    public synchronized void failed() {
        this.failedRequests++;
    }

    public int getSucc() {
        return this.succRequests;
    }

    public int getFailed() {
        return this.failedRequests;
    }

    public static HttpCounter getInstance() {
        if (httpCounterInstance == null) {
            httpCounterInstance = new HttpCounter();
        }
        return httpCounterInstance;
    }
}
