package com.bsds.server;

import com.bsds.server.db.LiftRideEntity;

import org.springframework.data.repository.CrudRepository;

// @Repository
public interface LiftRideRepository extends CrudRepository<LiftRideEntity, Integer> {

}
