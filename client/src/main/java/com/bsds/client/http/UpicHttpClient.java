package com.bsds.client.http;

import java.io.IOException;
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
 * HttpClient for Upic Ski Resort 
 */
public class UpicHttpClient {

    private static final Logger logger = LogManager.getLogger(UpicHttpClient.class);

    private static HttpClient httpClientInstance;

    // gson converts pojo to json string
    private static Gson gson = new Gson();

    // HttpClient uses a Singleton pattern
    public static HttpClient getInstance() {
        if (httpClientInstance == null) {
            httpClientInstance = HttpClient.newBuilder().build();
        }
        return httpClientInstance;
    }

    /**
     * Post a lift ride to the Upic Ski Resort server
     * @param url - url to send the POST request to
     * @param time - time of lift ride 
     * @param liftID - lift id of the lift taken
     * @throws InterruptedException
     * @throws IOException
     */
    public static void postWriteLiftRide(String url, int time, int liftID) throws IOException, InterruptedException  {

        // create lift ride POJO and convert to JSON string 
        LiftRide liftRide = new LiftRide(time, liftID);
        String body = gson.toJson(liftRide);

        // construct request, send it and get response code 
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(url)).POST(BodyPublishers.ofString(body)).build();
        HttpResponse<String> response = UpicHttpClient.getInstance().send(postRequest, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();

        logResponse(responseCode);
    }


    public static void getVerticalForSkiDay(String url) throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = UpicHttpClient.getInstance().send(getRequest, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();

        logResponse(responseCode);
    }

    public static void logResponse(int responseCode){
        // check response code and log any 4xx or 5xx errors
        List<HttpStatus> values = Arrays.asList(HttpStatus.values());
        for (HttpStatus status : values) {
            if (responseCode == status.value()) {

                HttpStatus.Series httpStatusSeries = status.series();

                if (httpStatusSeries.equals(HttpStatus.Series.SUCCESSFUL)) {
                    HttpCounter.incrementNumSuccessful();
                } else if (httpStatusSeries.equals(HttpStatus.Series.CLIENT_ERROR)) {
                    HttpCounter.incrementNumFailed();
                    logger.error("4xx client error. Moving to next request.");
                } else if (httpStatusSeries.equals(HttpStatus.Series.SERVER_ERROR)) {
                    HttpCounter.incrementNumFailed();
                    logger.error("5xx server error. Moving to next request.");
                }
            }
        }
    };

}
