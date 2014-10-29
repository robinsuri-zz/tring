package com.example.robinsuri.tring;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

/**
 * Created by robinsuri on 10/28/14.
 */
public interface ISendHttpRequest {
    public void sendPostRequest(final HttpPost postRequest, final HttpRequestCallback callback);
    public void sendGetrequest(HttpGet httpget);


    public interface HttpRequestCallback {
        void httpResponse(HttpResponse httpresponse) throws IOException;
    }
}
