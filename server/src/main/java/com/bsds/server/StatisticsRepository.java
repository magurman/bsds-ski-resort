package com.bsds.server;

import com.bsds.server.db.StatisticsEntity;

import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<StatisticsEntity, Integer>{

    
}