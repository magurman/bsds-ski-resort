package com.bsds.client.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

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

        List<HttpStatus> values = Arrays.asList(HttpStatus.values());

        int responseCode = response.statusCode();

        for (HttpStatus status : values) {
            if (responseCode == status.value()) {

                HttpStatus.Series httpStatusSeries = status.series();

                if (httpStatusSeries.equals(HttpStatus.Series.SUCCESSFUL)) {
                    HttpCounter.getInstance().succ();
                } else if (httpStatusSeries.equals(HttpStatus.Series.CLIENT_ERROR)) {
                    HttpCounter.getInstance().failed();
                    logger.error("4xx client error. Moving to next request.");
                } else if (httpStatusSeries.equals(HttpStatus.Series.SERVER_ERROR)) {
                    HttpCounter.getInstance().failed();
                    logger.error("5xx server error. Moving to next request.");
                }
            }
        }
    }

}
