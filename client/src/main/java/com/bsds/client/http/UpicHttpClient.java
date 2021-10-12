package com.bsds.client.http;


import okhttp3.*;
//import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
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

        MediaType JSON = MediaType.parse("application/json");
        LiftRide liftRide = new LiftRide(time, liftID);
        //RequestBody body = RequestBody.create(gson.toJson(liftRide), JSON);
        String body = gson.toJson(liftRide);

        BodyPublisher bodyPublisher;
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(url)).POST(BodyPublishers.ofString(body)).build();

        // Response response = BSDSHttpClient.getInstance().newCall(postRequest).execute();
        HttpResponse<String> response = UpicHttpClient.getInstance().send(postRequest, HttpResponse.BodyHandlers.ofString());
     
        logger.info("RESPONSE STATUS CODE: " + response.statusCode());

        //System.out.println("RESPONSE: " + response);
    }

}
