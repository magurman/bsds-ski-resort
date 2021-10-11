package com.bsds.client.http;


import okhttp3.*;
//import org.json.JSONObject;

import com.bsds.client.model.LiftRide;
import com.google.gson.Gson;

/**
 * HttpClient for 
 */
public class HttpClient {
    public final OkHttpClient httpClient;

    // gson converts pojo to json string
    private Gson gson = new Gson();

    public HttpClient() {
        this.httpClient = new OkHttpClient();
    }

    public void postWriteLifeRide(String url, int time, int liftID) throws Exception {

        MediaType JSON = MediaType.parse("application/json");
        LiftRide liftRide = new LiftRide(time, liftID);
        RequestBody body = RequestBody.create(gson.toJson(liftRide), JSON);

        Request postRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = httpClient.newCall(postRequest).execute();
        System.out.println("RESPONSE: " + response);
    }

    public OkHttpClient getClient() {
        return this.httpClient;
    }

}
