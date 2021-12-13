package com.bsds.client.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.LogManager;

import com.bsds.client.ClientApplication;
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
    // public static void postWriteLiftRide(String url, int time, int liftID) throws IOException, InterruptedException  {

    //     // create lift ride POJO and convert to JSON string 
    //     LiftRide liftRide = new LiftRide(time, liftID);
    //     String body = gson.toJson(liftRide);

    //     String encodedCredentials = UpicHttpClient.getEncodedCredentials();
    //     HttpRequest postRequest = HttpRequest.newBuilder().header("Authorization", "Basic " + encodedCredentials).uri(URI.create(url)).POST(BodyPublishers.ofString(body)).build();
    //     HttpResponse<String> response = UpicHttpClient.getInstance().send(postRequest, HttpResponse.BodyHandlers.ofString());
    //     int responseCode = response.statusCode();

    //     logResponse(responseCode);
    // }

    public static HttpResponse<String> getLiftRidesForSkier(String url) {
        HttpRequest request = buildHttpRequest(url, null, HttpMethod.GET, null);

        HttpResponse<String> response;
        try {
            response = UpicHttpClient.getInstance().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("request failed.");
            e.printStackTrace();
            return null;
        }

        logResponse(response.statusCode());
        return response;
    }

    public static HttpResponse<String> postLiftRide(String url, LiftRide liftRide) {
        String body = gson.toJson(liftRide);

        String encodedCredentials = UpicHttpClient.getEncodedCredentials();
        // HttpRequest postRequest = HttpRequest.newBuilder().header("Authorization", "Basic " + encodedCredentials).uri(URI.create(url)).POST(BodyPublishers.ofString(body)).build();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedCredentials);

        HttpRequest request = buildHttpRequest(url, headers, HttpMethod.POST, body);
        
        HttpResponse<String> response;
        try {
            response = UpicHttpClient.getInstance().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("request failed.");
            e.printStackTrace();
            return null;
        }

        logResponse(response.statusCode());
        return response;
    }

    public static String getEncodedCredentials(){
        // construct request, send it and get response code 
        String authorization = "admin:admin";
        String encodedCredentials = Base64.getEncoder().encodeToString(authorization.getBytes());
        return encodedCredentials;
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
    }

    private static HttpRequest buildHttpRequest(String url, HashMap<String, String> headers, HttpMethod httpMethod, String body) {

        Builder httpRequestBuilder = HttpRequest.newBuilder();

        if (headers != null) {
            headers.forEach((k, v) -> httpRequestBuilder.header(k, v));
        }
        
        httpRequestBuilder.uri(URI.create(url));

        switch (httpMethod) {
            case DELETE:
                break;
            case GET:
                httpRequestBuilder.GET();
            case HEAD:
                break;
            case OPTIONS:
                break;
            case PATCH:
                break;
            case POST:
                httpRequestBuilder.POST(BodyPublishers.ofString(body));
            case PUT:
                break;
            case TRACE:
                break;
            default:
                break;
            
        }

        return httpRequestBuilder.build();
    }
}
