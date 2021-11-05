package com.bsds.server;

import java.util.ArrayList;

import com.bsds.server.db.LiftRideEntity;

import org.springframework.data.repository.CrudRepository;

public interface LiftRideRepository extends CrudRepository<LiftRideEntity, Integer> {

  ArrayList<LiftRideEntity> findBySkier_skierID(Integer skierID);

  ArrayList<LiftRideEntity> findBySkier_skierIDAndDayID(Integer skierID, Integer dayID);

  ArrayList<LiftRideEntity> findBySkier_skierIDAndSeason(Integer skierID, String season);

}
