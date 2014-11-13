package com.example.robinsuri.tring;

import android.util.Log;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by robinsuri on 10/30/14.
 */
public class ZeusClient implements IZeusClient {
    String getOrCreateUrl = "kujo.app/zeus/1.0/getOrCreateProfile";
    String sessionUrl = "kujo.app/zeus/1.0/mcSessionInitiate";
    String stagingUrl;
    String authentiateUrl = "kujo.app/zeus/1.0/authenticate";


    final Gson gson = new Gson();
    final SendHttpRequestImpl sendhttprequest = new SendHttpRequestImpl();

    @Override
    public void setGetOrCreateUrl(String getOrCreateUrl) {
        this.getOrCreateUrl = getOrCreateUrl;
    }

    @Override
    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }


    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }

    @Override
    public ListenableFuture<HttpResponse> createAccount(String firstName, String lastName, String number, String emailId) {
        final GetorCreateJson getorCreateJson = generateGetorCreateProfileJson(firstName, lastName, number, emailId);

        String getorcreategson = gson.toJson(getorCreateJson);
        Log.d("Tring", "Gson serialized string : " + getorcreategson);

        final HttpPost postRequest = preparePostRequest(stagingUrl + getOrCreateUrl, getorcreategson);

        final ListenableFuture<HttpResponse> future = sendhttprequest.sendPostRequest(postRequest);
        return future;

    }


    @Override
    public ListenableFuture<HttpResponse> getSessionMappingFromGuid(String guid) {


        SessionRequestGson sessionRequestGson = new SessionRequestGson();
        sessionRequestGson.setGuid(guid);
        String jsonSessionRequest = gson.toJson(sessionRequestGson);
        final HttpPost sessionPostRequest = preparePostRequest(stagingUrl + sessionUrl, jsonSessionRequest);
        ListenableFuture<HttpResponse> future = sendhttprequest.sendPostRequest(sessionPostRequest);
        return future;
//        Futures.addCallback(future, new FutureCallback<HttpResponse>() {
//            @Override
//            public void onSuccess(HttpResponse httpresponse) {
//                HttpEntity entity = httpresponse.getEntity();
//                String responseString = null;
//                try {
//                    responseString = EntityUtils.toString(entity, "UTF-8");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                JSONObject jsonResponse = null;
//                try {
//                    jsonResponse = new JSONObject(responseString);
//                    Log.d("Tring", "json response : " + jsonResponse);
//                    String mapping = (String) jsonResponse.get("mapping");
//                    String sessionId = (String) jsonResponse.get("sessionId");
//                    callbackForSessionCreate.sessionCallback(mapping, sessionId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    callbackForSessionCreate.handleError(e, e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });

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
        getorCreateJson.set_namejson(nameJson);
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

    ListenableFuture<HttpResponse> authenticate(String guid, String sessionId) {
        Log.d("ZeusClient", "Inside authenticate");
        AuthenticateGson authenticateGson = new AuthenticateGson();
        authenticateGson.setGuid(guid);
        authenticateGson.setSessionId(sessionId);
        String jsonAuthenticateRequest = gson.toJson(authenticateGson);
        final HttpPost authenticatePostRequest = preparePostRequest(stagingUrl + authentiateUrl, jsonAuthenticateRequest);
        final String[] token2 = new String[1];
        ListenableFuture<HttpResponse> future = sendhttprequest.sendPostRequest(authenticatePostRequest);
        return future;

    }

}
