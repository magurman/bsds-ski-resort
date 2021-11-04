package com.bsds.server;

import java.util.ArrayList;

import com.bsds.server.db.StatisticsEntity;

import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<StatisticsEntity, Integer>{
    
    ArrayList<StatisticsEntity> findByURLAndOperation(String URL, String operation);
    
}