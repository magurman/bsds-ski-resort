package com.bsds.server.repository;

import com.bsds.server.db.StatisticsEntity;

import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<StatisticsEntity, Integer>{
    
    StatisticsEntity findByURLAndOperation(String URL, String operation);   
}