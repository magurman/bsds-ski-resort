package com.bsds.server.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsds.server.model.ResponseMessage;
import com.bsds.server.model.SkierVertical;
import com.bsds.server.db.LiftEntity;
import com.bsds.server.db.LiftRideEntity;
import com.bsds.server.db.ResortEntity;
import com.bsds.server.db.SkierEntity;
import com.bsds.server.db.StatisticsEntity;
import com.bsds.server.db.UpicDbHelper;
import com.bsds.server.model.LiftRide;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SkierServlet {
    private static final String PATH_PREFIX = "/skiers";

    // gson converts pojo to json string
    private Gson gson = new Gson();

    @Autowired
    private UpicDbHelper upicDbHelper;
    
    /**
     * 
     * @param resortID
     * @param seasonID
     * @param dayID
     * @param skierID
     * @param req
     * @param res
     * @throws IOException
     */
    @PostMapping(PATH_PREFIX + "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    void writeLiftRide(@PathVariable int resortID, @PathVariable String seasonID, @PathVariable int dayID, @PathVariable int skierID,
                     HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        long startTime = System.currentTimeMillis(); // for calculating latency
        
        final String[] creds = getCredentialsFromRequest(req); // get auth credentials from request and perform default authorization test

        // validate credentials 
        if (creds.length == 0 || !creds[0].equals("admin") || !creds[1].equals("admin")) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.WWW_AUTHENTICATE, "invalid authorization credentials");
            String messageJson = gson.toJson(new ResponseMessage("invalid username/password provided. Please retry with new credentials."));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.UNAUTHORIZED.value(), MediaType.APPLICATION_JSON_VALUE, headers);
            return;
        }

        // validate dayID parameter
        if (!validateDayID(dayID)) {
            String messageJson = gson.toJson(new ResponseMessage("invalid day ID!"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }

        // validate request body
        // this only works for application/json content type
        LiftRide liftRide;
        try {
            byte[] inputStreamBytes = req.getInputStream().readAllBytes();
            String reqBody = new String(inputStreamBytes, StandardCharsets.UTF_8);
            liftRide = gson.fromJson(reqBody, LiftRide.class);
        } catch (Exception e) {
            String messageJson = gson.toJson(new ResponseMessage("invalid request body! Req body must be LiftRide json object!"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }
            
        // ResortEntity resortEntity = upicDbHelper.findResortEntityById(resortID); // lookup if resort exists in db using resort ID from request   
        // if (resortEntity == null) {
        //     String resortName = "Resort " + resortID; // will need some way to lookup name from id
        //     resortEntity = upicDbHelper.createResortEntity(resortID, resortName); 
        //     upicDbHelper.saveResortEntity(resortEntity); // save resort entity to db
        // }

        // LiftEntity liftEntity = upicDbHelper.findLiftEntityById(liftRide.liftID); // lookup if resort exists in db using liftID from request body
        // if (liftEntity == null) {
        //     liftEntity = upicDbHelper.createLiftEntity(liftRide.liftID, resortEntity, 1, 10); // will need some way to determine number from id and vertical distance
        //     upicDbHelper.saveLiftEntity(liftEntity); // save lift entity to db
        // }

        // SkierEntity skierEntity = upicDbHelper.findSkierEntityById(skierID); // lookup skier in db with id from request path
        // if (skierEntity == null) {
        //     skierEntity = upicDbHelper.createSkierEntity(skierID); //create skier entity if it doesn't exist in db
        //     upicDbHelper.saveSkierEntity(skierEntity); // save skier entity to db
        // }

        ResortEntity rEntity = new ResortEntity();
        rEntity.setResortID(resortID);
        LiftEntity lEntity = new LiftEntity();
        lEntity.setLiftID(liftRide.lift);
        lEntity.setResort(rEntity);
        SkierEntity sEntity = new SkierEntity();
        sEntity.setSkierID(skierID);

        LiftRideEntity liftRideEntity = upicDbHelper.createLiftRideEntity(dayID, liftRide.time, "2021", lEntity, sEntity); // create lift ride entity
        upicDbHelper.saveLiftRideEntity(liftRideEntity); // save lift ride entity to db

        ServletUtils.formatHttpResponse(res, null, HttpStatus.CREATED.value(), null, null);

        // long latency = System.currentTimeMillis() - startTime;
        // this.updateStatistics(latency, HttpMethod.POST.name(), "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");
    
    }

    @GetMapping(PATH_PREFIX + "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public void getVerticalForSkiDay(@PathVariable int resortID, @PathVariable String seasonID, @PathVariable int dayID, @PathVariable int skierID,
                    HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        long startTime = System.currentTimeMillis(); // for calculating latency

        // validate dayID parameter
        if (!validateDayID(dayID)) {
            String messageJson = gson.toJson(new ResponseMessage("invalid day ID!"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }

        ArrayList<LiftRideEntity> liftRides = upicDbHelper.findLiftRideBySkierIdAndDayId(skierID, dayID);

        if (liftRides.size() == 0) {
            String messageJson = gson.toJson(new ResponseMessage("no lift rides were found for skier: " + skierID + "and day: " + dayID));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.NOT_FOUND.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }

        Integer total_vertical_distance = 0;

        for (LiftRideEntity lr : liftRides) {
            LiftEntity lift = lr.getLift();
            total_vertical_distance = total_vertical_distance + lift.getVerticalDistance();
        }

        ServletUtils.formatHttpResponse(res, Integer.toString(total_vertical_distance), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);

        // long latency = System.currentTimeMillis() - startTime;
        // this.updateStatistics(latency, HttpMethod.GET.name(), "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");

    }

    @GetMapping(PATH_PREFIX + "/{skierID}/vertical")
    public void vertical(@PathVariable int skierID, HttpServletRequest req, HttpServletResponse res) throws IOException {

        // get and validate existence of resort query param -- it is required
        String resortQueryParamter = req.getParameter("resort");
        if (resortQueryParamter == null) {
            String messageJson = gson.toJson(new ResponseMessage("missing required resort parameter!"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }

        String seasonQueryParameter = req.getParameter("season"); // season is not required --  will be null if not included

        // Integer totalVertical = 0;
        List<SkierVertical> skierVerticalList = new ArrayList<>();

        if (seasonQueryParameter != null) {
            ArrayList<LiftRideEntity> liftRides = upicDbHelper.findLiftRideBySkierIdAndSeason(skierID, seasonQueryParameter);

            if (liftRides.size() == 0) {
                String messageJson = gson.toJson(new ResponseMessage("no lift rides found for skier " + skierID));
                ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.NOT_FOUND.value(), MediaType.APPLICATION_JSON_VALUE, null);
                return;
            }

            List<LiftRideEntity> fileredLiftRides = liftRides.stream().filter(r -> r.getLift().getResort().getResortID() == Integer.parseInt(resortQueryParamter)).collect(Collectors.toList());

            Integer totalVertical = fileredLiftRides.stream().mapToInt((l)-> l.getLift().getVerticalDistance()).sum();

            skierVerticalList.add(new SkierVertical(seasonQueryParameter, totalVertical)); 
        } else {
            ArrayList<LiftRideEntity> liftRides = upicDbHelper.findLiftRideBySkierId(skierID);
            List<LiftRideEntity> fileredLiftRides = liftRides.stream().filter(r -> r.getLift().getResort().getResortID() == Integer.parseInt(resortQueryParamter)).collect(Collectors.toList());

            HashMap<String, Integer> seasonVerticalMap = new HashMap<>();

            for (LiftRideEntity liftRide : fileredLiftRides) {
                if (seasonVerticalMap.containsKey(liftRide.getSeason())) {
                    Integer currentVertical = seasonVerticalMap.get(liftRide.getSeason());
                    currentVertical = currentVertical + liftRide.getLift().getVerticalDistance();
                    seasonVerticalMap.put(liftRide.getSeason(), currentVertical);
                } else {
                    Integer currentVertical = liftRide.getLift().getVerticalDistance();
                    seasonVerticalMap.put(liftRide.getSeason(), currentVertical);
                }
            }

            for (Map.Entry<String, Integer> entry : seasonVerticalMap.entrySet()) {
                skierVerticalList.add(new SkierVertical(entry.getKey(), entry.getValue()));
            }

        }

        String finalSkierVerticalList = gson.toJson(skierVerticalList);
        ServletUtils.formatHttpResponse(res, finalSkierVerticalList, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }

    /**
     * Private method to update a statistics entity in the db
     * @param latency
     * @param operation
     * @param url
     */
    // private synchronized void updateStatistics(float latency, String operation, String url) {
    //     StatisticsEntity currentStatistics = upicDbHelper.findStatisticsByURLAndOperation(url, operation); // get statistics for this url and http operation
        
    //     if(currentStatistics != null){ // update existing entry
    //         float currentAverageLatency = currentStatistics.getAverageLatency();
    //         int currentTotalNumRequests = currentStatistics.getTotalNumRequests();
    //         float currentMaximumLatency = currentStatistics.getMaxLatency();

    //         if(latency > currentMaximumLatency){
    //             currentStatistics.setMaxLatency(latency);
    //         }

    //         float newAverageLatency = (currentAverageLatency * currentTotalNumRequests + latency)/ ((float) currentTotalNumRequests + 1);
    //         currentStatistics.setAverageLatency(newAverageLatency);
    //         currentStatistics.setTotalNumRequests(++currentTotalNumRequests);

    //         upicDbHelper.saveStatistics(currentStatistics);
    //     } else { // create new statistics 
    //         StatisticsEntity newStat = upicDbHelper.createStatisticsEntity(latency, latency, 1, url, operation);
    //         upicDbHelper.saveStatistics(newStat);
    //     }
    // }

    /**
     * Private method to validate a dayID. Day IDs must be between 1 and 366
     * @param dayID
     * @return boolean, true if valid dayID
     */
    private boolean validateDayID(int dayID) {
        return dayID > 0 && dayID < 367;
    }

    /**
     * Get credentials from HttpServletRequest and return them as a String[] in the format: 
     * ["username", "password"]
     * 
     * This method currently only supports Basic auth
     * @param req - the request which contains authentication credentials 
     * @return the authentication credentials extracted from the request
     */
    private String[] getCredentialsFromRequest(HttpServletRequest req) {

        final String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        String[] credValues = {};
        // for now, only support Basic auth
        if (auth != null && auth.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = auth.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String creds = new String(credDecoded, StandardCharsets.UTF_8);

            credValues = creds.split(":", 2);
        }

        return credValues;
    }
}
