package com.bsds.server.db;

import java.util.ArrayList;
import java.util.Optional;

import com.bsds.server.LiftRepository;
import com.bsds.server.LiftRideRepository;
import com.bsds.server.ResortRepository;
import com.bsds.server.SkierRepository;
import com.bsds.server.StatisticsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

/**
 * Database helper class to wrap all the database operations for upic ski resort
 */
public class UpicDbHelper {
    @Autowired
    private LiftRideRepository liftRideRepository;

    @Autowired
    private LiftRepository liftRepository;

    @Autowired
    private ResortRepository resortRepository;

    @Autowired
    private SkierRepository skierRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    public UpicDbHelper() {}

    @Cacheable("resort")
    public ResortEntity findResortEntityById(Integer resortID) {
        Optional<ResortEntity> resortEntityResult = resortRepository.findById(resortID);
        ResortEntity resortEntity = resortEntityResult.isPresent() ? resortEntityResult.get() : null;
        return resortEntity;
    }

    public ResortEntity createResortEntity(Integer resortID, String name) {
        ResortEntity resortEntity = new ResortEntity();
        resortEntity.setResortID(resortID);
        resortEntity.setName(name);
        return resortEntity;
    }

    public void saveResortEntity(ResortEntity resortEntity) {
        this.resortRepository.save(resortEntity);
    }

    @Cacheable("lift")
    public LiftEntity findLiftEntityById(Integer liftID) {
        Optional<LiftEntity> liftEntityResult = liftRepository.findById(liftID);
        LiftEntity liftEntity = liftEntityResult.isPresent() ? liftEntityResult.get() : null;
        return liftEntity;
    }

    public LiftEntity createLiftEntity(Integer liftID, ResortEntity resortEntity, Integer liftNumber, Integer verticalDistance) {
        LiftEntity liftEntity = new LiftEntity();
        liftEntity.setLiftID(liftID);
        liftEntity.setResort(resortEntity);
        liftEntity.setLiftNumber(liftNumber); // will need some way to determine number from id? 
        liftEntity.setVerticalDistance(verticalDistance); // should be derived from number 

        return liftEntity;
    }

    public void saveLiftEntity(LiftEntity liftEntity) {
        this.liftRepository.save(liftEntity);
    }

    @Cacheable("skier")
    public SkierEntity findSkierEntityById(Integer skierID) {
        Optional<SkierEntity> skierEntityResult = this.skierRepository.findById(skierID);
        SkierEntity skierEntity = skierEntityResult.isPresent() ? skierEntityResult.get() : null;
        return skierEntity;
    }

    public SkierEntity createSkierEntity(Integer skierID) {
        SkierEntity newSkier = new SkierEntity();
        newSkier.setSkierID(skierID);
        return newSkier;
    }

    public void saveSkierEntity(SkierEntity skierEntity) {
        this.skierRepository.save(skierEntity);
    }

    public LiftRideEntity findLiftRideById(Integer liftRideId) {
        Optional<LiftRideEntity> liftRideEntityResult = this.liftRideRepository.findById(liftRideId);
        LiftRideEntity liftRideEntity = liftRideEntityResult.isPresent() ? liftRideEntityResult.get() : null;
        return liftRideEntity;
    }

    public ArrayList<LiftRideEntity> findLiftRideBySkierId(Integer skierID) {
        return this.liftRideRepository.findBySkier_skierID(skierID);
    }

    public ArrayList<LiftRideEntity> findLiftRideBySkierIdAndDayId(Integer skierID, Integer dayID) {
        return this.liftRideRepository.findBySkier_skierIDAndDayID(skierID, dayID);
    }

    public ArrayList<LiftRideEntity> findLiftRideBySkierIdAndSeason(Integer skierID, String season) {
        return this.liftRideRepository.findBySkier_skierIDAndSeason(skierID, season);
    }

    public LiftRideEntity createLiftRideEntity(Integer dayID, Integer time, String season, LiftEntity liftEntity, SkierEntity skierEntity){
        LiftRideEntity liftRideEntity = new LiftRideEntity();
        liftRideEntity.setTime(time);
        liftRideEntity.setLift(liftEntity);
        liftRideEntity.setDayID(dayID);
        liftRideEntity.setSeason(season);
        liftRideEntity.setSkier(skierEntity);

        return liftRideEntity;
    }

    public void saveLiftRideEntity(LiftRideEntity liftRideEntity) {
        this.liftRideRepository.save(liftRideEntity);
    }

    public ArrayList<ResortEntity> findAllResorts() {
        return (ArrayList<ResortEntity>) this.resortRepository.findAll();
    }

    public ArrayList<StatisticsEntity> findAllStatistics(){
        return (ArrayList<StatisticsEntity>) this.statisticsRepository.findAll();
    }

    public synchronized StatisticsEntity findStatisticsByURLAndOperation(String URL, String operation){
        return this.statisticsRepository.findByURLAndOperation(URL, operation);
    }

    public StatisticsEntity createStatisticsEntity(float maxLatency, float averageLatency, int totalNumRequests, String URL, String operation) {
        StatisticsEntity newStat = new StatisticsEntity();
        newStat.setMaxLatency(maxLatency);
        newStat.setTotalNumRequests(1);
        newStat.setURL(URL);
        newStat.setOperation(operation);
        newStat.setAverageLatency(averageLatency);

        return newStat;
    }

    public void saveStatistics(StatisticsEntity stats){
        this.statisticsRepository.save(stats);
    }
}
