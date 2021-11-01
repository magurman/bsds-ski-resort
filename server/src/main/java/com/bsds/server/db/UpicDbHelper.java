package com.bsds.server.db;

import java.util.Optional;

import com.bsds.server.LiftRepository;
import com.bsds.server.LiftRideRepository;
import com.bsds.server.ResortRepository;
import com.bsds.server.SkierRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class UpicDbHelper {
    @Autowired
    private LiftRideRepository liftRideRepository;

    @Autowired
    private LiftRepository liftRepository;

    @Autowired
    private ResortRepository resortRepository;

    @Autowired
    private SkierRepository skierRepository;

    public UpicDbHelper() {

    }

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

    public SkierEntity findSkierEntityById(Integer skierID) {
        Optional<SkierEntity> skierEntityResult = this.skierRepository.findById(skierID);
        SkierEntity skierEntity = skierEntityResult.isPresent() ? skierEntityResult.get() : null;
        return skierEntity;
    }

    public SkierEntity createSkierEntity() {
        return new SkierEntity();
    }

    public void saveSkierEntity(SkierEntity skierEntity) {
        this.skierRepository.save(skierEntity);
    }


    public LiftRideEntity createLiftRideEntity(Integer dayID, Integer time, String season, LiftEntity liftEntity, SkierEntity skierEntity){
        LiftRideEntity liftRideEntity = new LiftRideEntity();
        liftRideEntity.setTime(time);
        liftRideEntity.setLift(liftEntity);
        liftRideEntity.setDayID(dayID);
        liftRideEntity.setSeason("2021");
        liftRideEntity.setSkier(skierEntity);

        return liftRideEntity;
    }

    public void saveLiftRideEntity(LiftRideEntity liftRideEntity) {
        this.liftRideRepository.save(liftRideEntity);
    }

}
