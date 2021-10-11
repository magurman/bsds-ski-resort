package com.bsds.client.http;


import okhttp3.*;
//import org.json.JSONObject;

import com.bsds.client.model.LiftRide;
import com.google.gson.Gson;

/**
 * HttpClient for 
 */
public class HttpClient {

    private static OkHttpClient httpClientInstance;

    // gson converts pojo to json string
    private static Gson gson = new Gson();


    public static OkHttpClient getInstance(){
        if( httpClientInstance == null){
            httpClientInstance = new OkHttpClient();
        }
        return httpClientInstance;
    }

    public static void postWriteLifeRide(String url, int time, int liftID) throws Exception {

        MediaType JSON = MediaType.parse("application/json");
        LiftRide liftRide = new LiftRide(time, liftID);
        RequestBody body = RequestBody.create(gson.toJson(liftRide), JSON);

        Request postRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = HttpClient.getInstance().newCall(postRequest).execute();
        System.out.println("RESPONSE: " + response);
    }

}
