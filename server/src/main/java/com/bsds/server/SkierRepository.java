package com.bsds.server;

import com.bsds.server.db.SkierEntity;

import org.springframework.data.repository.CrudRepository;

public interface SkierRepository extends CrudRepository<SkierEntity, Integer> {
    
}
