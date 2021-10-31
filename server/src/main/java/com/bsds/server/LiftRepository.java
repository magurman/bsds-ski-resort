package com.bsds.server;

import com.bsds.server.db.LiftEntity;

import org.springframework.data.repository.CrudRepository;

public interface LiftRepository extends CrudRepository<LiftEntity, Integer> {

}