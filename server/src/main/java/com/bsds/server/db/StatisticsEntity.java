package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Statistics")
public class StatisticsEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer statisticsId;

    private String endpointName;

    private float averageLatency;

    private float maxLatency;

    private Integer totalNumRequests;

    public void setStatisticsId(Integer statisticsId){
        this.statisticsId = statisticsId;
    }

    public Integer getStatisticsId(){
        return this.statisticsId;
    }

    public void setEndpointName(String endpointName){
        this.endpointName = endpointName;
    }

    public String getEndpointName(){
        return this.endpointName;
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

    public void setTotalNumRequests(Integer totalNumRequests){
        this.totalNumRequests = totalNumRequests;
    }

    public int getTotalNumRequests(){
        return this.totalNumRequests;
    }

}