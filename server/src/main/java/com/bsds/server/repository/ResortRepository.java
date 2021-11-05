package com.bsds.server.repository;

import com.bsds.server.db.ResortEntity;

import org.springframework.data.repository.CrudRepository;

public interface ResortRepository extends CrudRepository<ResortEntity, Integer> {
    
}
