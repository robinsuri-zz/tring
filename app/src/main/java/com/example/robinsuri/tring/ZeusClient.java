package com.example.robinsuri.tring;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by robinsuri on 10/29/14.
 */
public class ZeusClient {

    public void getMapping(String firstName, String lastName, String number, String emailId, final Tring.TestCallBack testcallback) {


        final GetorCreateJson getorCreateJson = new GetorCreateJson();
        getorCreateJson.setEmails(emailId);
        getorCreateJson.setMobiles(number);
        NameJson nameJson = new NameJson();
        nameJson.setFirstName(firstName);
        nameJson.setLastName(lastName);
        getorCreateJson.setNamejson(nameJson);


        final Gson gson = new Gson();
        String getorcreategson = gson.toJson(getorCreateJson);
        Log.d("Tring", "Gson serialized string : "+getorcreategson);

        String url = "https://proxy-staging-external.handler.talk.to/kujo.app/zeus/1.0/getOrCreateProfile";
        final HttpPost postRequest = preparePostRequest(url, getorcreategson);
        final SendHttpRequestImpl sendhttprequest = new SendHttpRequestImpl();


        final SendHttpRequestImpl.HttpRequestCallback sessionResponseCallback = new SendHttpRequestImpl.HttpRequestCallback() {
            @Override
            public void httpResponse(HttpResponse httpresponse) throws IOException {
                String EXTRA_MESSAGE = "message";
                HttpEntity entity = httpresponse.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(responseString);
                    String mapping = (String) jsonResponse.get("mapping");
                    testcallback.getItBack(mapping);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        ISendHttpRequest.HttpRequestCallback httpRequestCallback = new ISendHttpRequest.HttpRequestCallback() {
            @Override
            public void httpResponse(HttpResponse httpresponse) throws IOException {
                Log.d("Tring", httpresponse.toString());
                HttpEntity entity = httpresponse.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    Log.d("Tring","json response : "+jsonResponse );
                    String guid = (String) jsonResponse.get("guid");
                    Log.d("Tring", guid);
                    String sessionUrl = "https://proxy-staging-external.handler.talk.to/kujo.app/zeus/1.0/mcSessionInitiate";
                    SessionRequestGson sessionRequestGson = new SessionRequestGson();
                    sessionRequestGson.setGuid(guid);
                    String jsonSessionRequest = gson.toJson(sessionRequestGson);

                    final HttpPost sessionPostRequest = preparePostRequest(sessionUrl, jsonSessionRequest);
                    sendhttprequest.sendPostRequest(sessionPostRequest, sessionResponseCallback);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Tring", responseString);

            }
        };


        sendhttprequest.sendPostRequest(postRequest, httpRequestCallback);


    }

    HttpPost preparePostRequest(String url, String request) {
        final HttpPost postRequest = new HttpPost(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType(new BasicHeader("Content-Type", "application/json"));
        postRequest.setEntity(entity);
        return postRequest;
    }
}
