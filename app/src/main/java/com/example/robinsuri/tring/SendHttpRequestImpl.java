package com.example.robinsuri.tring;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by robinsuri on 10/28/14.
 */
public class SendHttpRequestImpl implements ISendHttpRequest {

    Executor executor = Executors.newSingleThreadExecutor();

    public void sendPostRequest(final HttpPost postRequest, final HttpRequestCallback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final HttpClient httpclient = new DefaultHttpClient();
                try {
                    Log.d("Tring", "inside sendRequest");
                    HttpResponse response = httpclient.execute(postRequest);
                    Log.d("Tring", "URI : " + postRequest.getURI());
                    callback.httpResponse(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void sendGetrequest(HttpGet httpget) {

    }


}
