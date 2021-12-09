package com.bsds.server.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.db.LiftRideEntity;

public class ServletUtils {

    static String getRequestBody(HttpServletRequest req) throws IOException {

        // byte[] inputStreamBytes = req.getInputStream().readAllBytes();
        // String reqBody = new String(inputStreamBytes, StandardCharsets.UTF_8);
        // return reqBody;

        String reqBody = "";   
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reqBody = builder.toString();

        // String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        return reqBody;
    }

    static void formatHttpResponse(HttpServletResponse res, String responseBody, int statusCode, String contentType, HashMap<String, String> headers) throws IOException {

        res.setStatus(statusCode);

        if (contentType != null) res.setContentType(contentType);
        if (responseBody != null) res.getWriter().append(responseBody);
        if (headers != null) {
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                res.addHeader(entry.getKey(), entry.getValue());
            }
        }
    } 
    
    static JsonObject formatLiftRideJson(LiftRideEntity liftRide, String url) {
        JsonObjectBuilder jsonB = Json.createObjectBuilder();
        jsonB.add("liftRideId", liftRide.getLiftRideID());
        jsonB.add("url", "/liftrides/" + Integer.toString(liftRide.getLiftRideID()));
        jsonB.add("skier", liftRide.getSkier().getSkierID());
        jsonB.add("resort", liftRide.getLift().getResort().getResortID());
        jsonB.add("lift", liftRide.getLift().getLiftID());
        jsonB.add("time", liftRide.getTime());
        return jsonB.build();
    }
}
