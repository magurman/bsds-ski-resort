package com.bsds.client.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import javax.swing.text.StyledEditorKit;

import java.net.http.HttpRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.bsds.client.model.LiftRide;
import com.google.gson.Gson;

/**
 * HttpClient for 
 */
public class UpicHttpClient {

    private static final Logger logger = LogManager.getLogger(UpicHttpClient.class);

    private static HttpClient httpClientInstance;

    // gson converts pojo to json string
    private static Gson gson = new Gson();

    public static HttpClient getInstance() {
        if (httpClientInstance == null) {
            httpClientInstance = HttpClient.newBuilder().build();
        }
        return httpClientInstance;
    }

    public static void postWriteLifeRide(String url, int time, int liftID) throws Exception {

        LiftRide liftRide = new LiftRide(time, liftID);
        String body = gson.toJson(liftRide);

        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(url)).POST(BodyPublishers.ofString(body)).build();

        HttpResponse<String> response = UpicHttpClient.getInstance().send(postRequest, HttpResponse.BodyHandlers.ofString());

        switch (response.statusCode()) {
            case 400:
                HttpCounter.getInstance().failed();
                logger.error("400 Bad Request. Moving to next request.");
            case 404:
                HttpCounter.getInstance().failed();
                logger.error("404 Not Found. Moving to next request.");
            case 500:
                HttpCounter.getInstance().failed();
                logger.error("500 Internal Server Error. Moving to next request.");
            case 201:
                HttpCounter.getInstance().succ();
                logger.info("Success.");
            default:
                // do nothing
        }
    }

}
