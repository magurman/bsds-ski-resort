package com.bsds.client;


import okhttp3.*;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;


public class HttpClient {
    public final OkHttpClient httpClient;

    public HttpClient() {
        this.httpClient = new OkHttpClient();
    }

    // TODO: Add Springboot Post tag - need to figure out if it is the same one as the server.
    @PostMapping
    public void postWriteLifeRide(int resortID, int seasonID, int dayID, int skierID) throws Exception {
//        String tempFullPath = "/{{resortID}}/seasons/{{seasonID}}/days/{{dayID}}/skiers/{{skierID}}";

        String postURL = "http://localhost:8080/skiers/" + resortID + "/seasons/" + seasonID + "/days/" + dayID + "/skiers/" + skierID;

        MediaType JSON = MediaType.parse("application/json");
        JSONObject postData = new JSONObject();
        postData.put("resortID", resortID);
        postData.put("seasonID", seasonID);
        postData.put("dayID", dayID);
        postData.put("skierID", skierID);

        RequestBody body = RequestBody.create(postData.toString(), JSON);

        Request postRequest = new Request.Builder()
                .url(postURL)
                .post(body)
                .build();

        Response response = httpClient.newCall(postRequest).execute();
        System.out.println(response);

    }

    public OkHttpClient getClient() {
        return this.httpClient;
    }

}
