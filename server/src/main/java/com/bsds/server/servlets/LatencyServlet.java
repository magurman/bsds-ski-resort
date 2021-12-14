package com.bsds.server.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class LatencyServlet {
    
    private static final String PATH_PREFIX = "/latency";

    @GetMapping(value = PATH_PREFIX)
    public void getLatency(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = req.getParameter("method");

        if (method == null) {
            ServletUtils.formatHttpResponse(res, "invalid method", HttpStatus.NOT_ACCEPTABLE.value(), MediaType.APPLICATION_JSON_VALUE, null);
        }

        ArrayList<Long> latency;
        switch (method) {
            case "get":
                latency = LatencyTracker.getLatency(HttpMethod.GET);
                break;
            case "post":
                latency = LatencyTracker.getLatency(HttpMethod.POST);
                break;
            default:
                latency = new ArrayList<>();
        }

        JsonObjectBuilder latencyJson = Json.createObjectBuilder();

        JsonArrayBuilder latencyArray = Json.createArrayBuilder(latency);

        latencyJson.add("latency", latencyArray.build());
        
        JsonObject returnJson = Json.createObjectBuilder().add("latency", latencyJson).build();

        ServletUtils.formatHttpResponse(res, returnJson.toString(), HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }

    @PutMapping(value=PATH_PREFIX)
    public void resetLatency(HttpServletRequest req, HttpServletResponse res) throws IOException {
        LatencyTracker.resetLatency();
        ServletUtils.formatHttpResponse(res, "responseBody", HttpStatus.ACCEPTED.value(), null, null);
    }
}
