package com.bsds.server;

import java.util.ArrayList;

import com.bsds.server.db.LiftRideEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import antlr.collections.List;

// @Repository
public interface LiftRideRepository extends CrudRepository<LiftRideEntity, Integer> {

  ArrayList<LiftRideEntity> findBySkier_skierID(Integer skierID);
}
