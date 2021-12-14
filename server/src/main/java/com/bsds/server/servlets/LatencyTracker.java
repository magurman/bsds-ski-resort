package com.bsds.server.servlets;

import java.util.ArrayList;

import org.springframework.http.HttpMethod;

public class LatencyTracker {
    private static ArrayList<Long> latencyGet = new ArrayList<>();
    private static ArrayList<Long> latencyPost = new ArrayList<>();


    static synchronized void addLatency(long latency, HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                latencyGet.add(latency);
            case POST:
                latencyPost.add(latency);
            default:
                break;
        }     
    }

    static ArrayList<Long> getLatency(HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                return latencyGet;
            case POST:
                return latencyPost;
            default:
                break;
        }
        return null;
    }

    static synchronized void resetLatency() {
        latencyGet = new ArrayList<>();
        latencyPost = new ArrayList<>();
    }
}
