package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "statistics")
public class StatisticsEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer statisticsId;

    private String operation;

    private float averageLatency;

    private float maxLatency;

    private Integer totalNumRequests;

    private String URL;

    public void setStatisticsId(Integer statisticsId){
        this.statisticsId = statisticsId;
    }

    @JsonIgnore
    public Integer getStatisticsId(){
        return this.statisticsId;
    }

    public void setOperation(String operation){
        this.operation = operation;
    }

    public String getOperation(){
        return this.operation;
    }

    public void setAverageLatency(float averageLatency){
        this.averageLatency = averageLatency;
    }

    public float getAverageLatency(){
        return this.averageLatency;
    }

    public void setMaxLatency(float maxLatency){
        this.maxLatency = maxLatency;
    }

    public float getMaxLatency(){
        return this.maxLatency;
    }

    @JsonIgnore
    public void setTotalNumRequests(Integer totalNumRequests){
        this.totalNumRequests = totalNumRequests;
    }

    public int getTotalNumRequests(){
        return this.totalNumRequests;
    }

    public void setURL(String URL){
        this.URL = URL;
    }

    public String getURL(){
        return this.URL;
    }

}