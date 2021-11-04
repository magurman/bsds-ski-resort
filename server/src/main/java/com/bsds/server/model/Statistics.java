package com.bsds.server.model;

public class Statistics {
    private String operation;
    private String URL;
    private float maxLatency;
    private float averageLatency;
    private int totalNumRequests;

    public Statistics(String operation, String URL, int totalNumRequests, float maxLatency, float averageLatency){
        this.operation = operation;
        this.URL = URL;
        this.maxLatency = maxLatency;
        this.averageLatency = averageLatency;
        this.totalNumRequests = totalNumRequests;
    }
}