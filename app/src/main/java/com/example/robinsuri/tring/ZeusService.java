package com.example.robinsuri.tring;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robinsuri on 10/30/14.
 */
public class ZeusService {
    String getOrCreateUrl;
    String sessionUrl;
    String stagingUrl;
    final Gson gson = new Gson();
    final SendHttpRequestImpl sendhttprequest = new SendHttpRequestImpl();

    public void setGetOrCreateUrl(String getOrCreateUrl) {
        this.getOrCreateUrl = getOrCreateUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }

    public void getMapping(String firstName, String lastName, String number, String emailId, final Tring.TestCallBack testcallback) {
        final GetorCreateJson getorCreateJson = generateGetorCreateProfileJson(firstName, lastName, number, emailId);

        String getorcreategson = gson.toJson(getorCreateJson);
        Log.d("Tring", "Gson serialized string : " + getorcreategson);

        final HttpPost postRequest = preparePostRequest(stagingUrl + getOrCreateUrl, getorcreategson);

        final sessionCallBack sessioncallback = new sessionCallBack() {
            @Override
            public void getmapping(String mapping) {
                testcallback.getItBack(mapping);
            }

            @Override
            public void handleError(Exception e, String message) {

            }
        };


        ISendHttpRequest.HttpRequestCallback getOrCreateProfileCallback = new ISendHttpRequest.HttpRequestCallback() {
            @Override
            public void httpResponse(HttpResponse httpresponse) throws IOException {
                Log.d("Tring", httpresponse.toString());
                HttpEntity entity = httpresponse.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    Log.d("Tring", "json response : " + jsonResponse);
                    String guid = (String) jsonResponse.get("guid");
                    Log.d("Tring", guid);
                    getSessionMappingFromGuid(guid, sessioncallback);


                } catch (JSONException e) {
                    e.printStackTrace();
                    testcallback.handlerError(e, e.getMessage());
                }

                Log.d("Tring", responseString);

            }
        };


        sendhttprequest.sendPostRequest(postRequest, getOrCreateProfileCallback);


    }

    public interface sessionCallBack {
        void getmapping(String mapping);

        void handleError(Exception e, String message);
    }

    private void getSessionMappingFromGuid(String guid, final sessionCallBack sessioncallback) {


        SessionRequestGson sessionRequestGson = new SessionRequestGson();
        sessionRequestGson.setGuid(guid);
        String jsonSessionRequest = gson.toJson(sessionRequestGson);
        final HttpPost sessionPostRequest = preparePostRequest(stagingUrl + sessionUrl, jsonSessionRequest);

        final SendHttpRequestImpl.HttpRequestCallback sessionResponseCallback = new SendHttpRequestImpl.HttpRequestCallback() {
            @Override
            public void httpResponse(HttpResponse httpresponse) throws IOException {

                HttpEntity entity = httpresponse.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(responseString);
                    String mapping = (String) jsonResponse.get("mapping");
                    sessioncallback.getmapping(mapping);
                } catch (JSONException e) {
                    e.printStackTrace();
                    sessioncallback.handleError(e, e.getMessage());
                }

            }
        };
        sendhttprequest.sendPostRequest(sessionPostRequest, sessionResponseCallback);

    }

    private GetorCreateJson generateGetorCreateProfileJson(String firstName, String lastName, String number, String emailId) {
        final GetorCreateJson getorCreateJson = new GetorCreateJson();
        List<String> emails = new ArrayList<String>();
        emails.add(emailId);
        getorCreateJson.setEmails(emails);
        List<String> mobiles = new ArrayList<String>();
        mobiles.add(number);
        getorCreateJson.setMobiles(mobiles);
        NameJson nameJson = new NameJson();
        nameJson.setFirstName(firstName);
        nameJson.setLastName(lastName);
        getorCreateJson.setNamejson(nameJson);
        return getorCreateJson;
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
