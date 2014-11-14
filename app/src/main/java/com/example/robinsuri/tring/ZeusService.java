package com.example.robinsuri.tring;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by robinsuri on 10/29/14.
 */
public class ZeusService {


    String stagingUrl;

    public static final String PREFS_NAME = "MyPrefsFile";
    ZeusClient zeusClient = new ZeusClient();

    public void setSharePreferencesData(SharedPreferences sharePreferencesData) {
        this.sharePreferencesData = sharePreferencesData;
    }

    SharedPreferences sharePreferencesData;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    public ListenableFuture<HttpResponse> createAccount(String firstName, String lastName, String number, String emailId) {

        zeusClient.setStagingUrl(stagingUrl);
        ListenableFuture<HttpResponse> future = zeusClient.createAccount(firstName, lastName, number, emailId);
        return future;
    }

    public ListenableFuture<HttpResponse> createSession(String guid) {

        ListenableFuture<HttpResponse> future = zeusClient.getSessionMappingFromGuid(guid);
        return future;

    }

    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }

    public ListenableFuture<String> getmapping(final String firstName, final String lastName, final String number, final String emailId) {
        final ListenableFuture<HttpResponse> future = createAccount(firstName, lastName, number, emailId);

        final ListenableFuture<String> future1 = Futures.transform(future, new AsyncFunction<HttpResponse, String>() {
            @Override
            public ListenableFuture<String> apply(HttpResponse httpResponse) {
                ListenableFuture<String> future2 = null;
                try {
                    Log.d("Tring", httpResponse.toString());
                    HttpEntity entity = httpResponse.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");

                    JSONObject jsonResponse = new JSONObject(responseString);
                    Log.d("Tring", "json response : " + jsonResponse);
                    String guid = (String) jsonResponse.get("guid");

                    persist(guid);


                    ListenableFuture<HttpResponse> future = createSession(guid);
                    future2 = Futures.transform(future, new Function<HttpResponse, String>() {
                        @Override
                        public String apply(HttpResponse httpResponse) {
                            HttpEntity entity = httpResponse.getEntity();
                            String responseString = null;
                            String mapping = null;
                            try {
                                responseString = EntityUtils.toString(entity, "UTF-8");

                                JSONObject jsonResponse = null;

                                jsonResponse = new JSONObject(responseString);

                                Log.d("Tring", "json response : " + jsonResponse);

                                mapping = (String) jsonResponse.get("mapping");

                                String sessionId = null;

                                sessionId = (String) jsonResponse.get("sessionId");

                                persist(mapping, sessionId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return mapping;
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return future2;
            }
        });

        return future1;
    }


    private void persist(String mapping, String sessionId) {
        SharedPreferences.Editor editor = Tring.settings.edit();
        editor.putString("number", mapping);
        editor.putString("sessionId", sessionId);
        editor.commit();
        Log.d("Tring", "Number from file : " + Tring.settings.getString("number", ""));
    }


    private void persist(String guid) {

        SharedPreferences.Editor editor = sharePreferencesData.edit();
        editor.putString("guid", guid);
        editor.commit();
        Log.d("ZeusService", "Guid from sharedPreferences : " + sharePreferencesData.getString("guid", ""));
    }

    public ListenableFuture<String> getToken(String guid, String sessionId) {

        final ZeusService zeusService = this;
        zeusClient.setStagingUrl(stagingUrl);
        final ListenableFuture<HttpResponse> future = zeusClient.authenticate(guid, sessionId);
        final Function<HttpResponse, String> extractFunction =
                new Function<HttpResponse, String>() {
                    public String apply(HttpResponse httpresponse) {
                        HttpEntity entity = httpresponse.getEntity();
                        String responseString = null;
                        try {
                            responseString = EntityUtils.toString(entity, "UTF-8");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonResponse = null;
                        String token = null;
                        try {
                            jsonResponse = new JSONObject(responseString);
                            Log.d("Tring", "json response : " + jsonResponse);
                            token = (String) jsonResponse.get("token");
                            Log.d("NumberActivity", "token : " + token);
                            Log.d("NumberActivity", "jsonResponse : " + responseString);
                            SharedPreferences.Editor editor = NumberActivity.settings.edit();
                            editor.putString("token", token);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return token;
                    }


                };
        return Futures.transform(future, extractFunction);
    }


}
