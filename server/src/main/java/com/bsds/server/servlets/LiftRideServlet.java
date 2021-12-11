package com.bsds.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.db.LiftEntity;
import com.bsds.server.db.LiftRideEntity;
import com.bsds.server.db.ResortEntity;
import com.bsds.server.db.SkierEntity;
import com.bsds.server.db.UpicDbHelper;
import com.bsds.server.model.LiftRide;
import com.bsds.server.model.ResponseMessage;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@RestController
public class LiftRideServlet {
    private static final String PATH_PREFIX = "/liftrides";

    // private static final Logger logger = LogManager.getLogger(LiftRideServlet.class);
    private static final Logger latencyLogger = LogManager.getLogger("LATENCY");

    // gson converts pojo to json string
    private Gson gson = new Gson();

    @Autowired
    private UpicDbHelper upicDbHelper;

    @PostMapping(value = PATH_PREFIX)
    public void postLiftRide(HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        long start = System.currentTimeMillis();

        String reqBody;
        try {
            reqBody = ServletUtils.getRequestBody(req);
        } catch (IOException e) {
            String messageJson = gson.toJson(new ResponseMessage("invalid request body! Req body must be LiftRide json object!"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }
        LiftRide newLiftRide = gson.fromJson(reqBody, LiftRide.class);

        ResortEntity rEntity = new ResortEntity();
        rEntity.setResortID(newLiftRide.resort);
        LiftEntity lEntity = new LiftEntity();
        lEntity.setLiftID(newLiftRide.lift);
        lEntity.setResort(rEntity);
        SkierEntity sEntity = new SkierEntity();
        sEntity.setSkierID(newLiftRide.skier);

        LiftRideEntity newLiftRideEntity = upicDbHelper.createLiftRideEntity(1, newLiftRide.time, "2021", lEntity, sEntity);
        LiftRideEntity returnEntity = upicDbHelper.saveLiftRideEntity(newLiftRideEntity);

        JsonObject liftRideJson = ServletUtils.formatLiftRideJson(returnEntity, "/liftrides/");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        jsonArrayBuilder.add(liftRideJson);

        String responseBody = Json.createObjectBuilder().add("rides", jsonArrayBuilder.build()).build().toString();

        ServletUtils.formatHttpResponse(res, responseBody, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);

        long latency = System.currentTimeMillis() - start;
        latencyLogger.info("Request latency (milliseconds): " + latency);
    }

    @GetMapping(value = PATH_PREFIX)
    public void getLiftRides(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // get skier query param -- it is not required
        String skierQueryParamter = req.getParameter("skier");

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        if (skierQueryParamter == null) {
            Iterator<LiftRideEntity> liftRides = upicDbHelper.findAllLiftRides().iterator();
            while (liftRides.hasNext()) {
                LiftRideEntity liftRide = liftRides.next();
                JsonObject liftRideJson = ServletUtils.formatLiftRideJson(liftRide, "/liftrides/");
                jsonArrayBuilder.add(liftRideJson);
            }

        } else {
            int skierId;

            try {
                skierId = Integer.parseInt(skierQueryParamter);
            } catch (NumberFormatException e) {
                System.out.println("Error: skier ID not an integer");
                skierId = -1;
            }

            ArrayList<LiftRideEntity> liftRides = upicDbHelper.findLiftRideBySkierId(skierId);

            for (LiftRideEntity liftRide : liftRides) {
                JsonObject liftRideJson = ServletUtils.formatLiftRideJson(liftRide, "/liftrides/");
                jsonArrayBuilder.add(liftRideJson);
            }

        }

        String responseBody = Json.createObjectBuilder().add("rides", jsonArrayBuilder.build()).build().toString();
        ServletUtils.formatHttpResponse(res, responseBody, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }

    @GetMapping(value = PATH_PREFIX + "/{id}")
    public void getLiftRideById(@PathVariable int id, HttpServletRequest req, HttpServletResponse res) throws IOException{

        LiftRideEntity liftRide = upicDbHelper.findLiftRideById(id);

        if (liftRide == null) {
            String messageJson = gson.toJson(new ResponseMessage("no lift ride with id: " + id + " was found"));
            ServletUtils.formatHttpResponse(res, messageJson, HttpStatus.NOT_FOUND.value(), MediaType.APPLICATION_JSON_VALUE, null);
            return;
        }

        JsonObject liftRideJson = ServletUtils.formatLiftRideJson(liftRide, "/liftrides/");

        String responseBody = liftRideJson.toString();
        ServletUtils.formatHttpResponse(res, responseBody, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }
}
