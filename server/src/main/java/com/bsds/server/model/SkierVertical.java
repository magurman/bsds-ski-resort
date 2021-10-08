package com.bsds.server.model;

/**
 * Java object to represent a ski resort.
 */
public class SkierVertical {
    private String seasonID;
    private int totalVert;

    public SkierVertical(String seasonID, int totalVert) {
        this.seasonID = seasonID;
        this.totalVert = totalVert;
    }
}
