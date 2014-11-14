package com.example.robinsuri.tring;

import com.google.common.util.concurrent.ListenableFuture;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

public interface ISendHttpRequest {
    public ListenableFuture<HttpResponse> sendPostRequest(final HttpPost postRequest);

    public void sendGetrequest(HttpGet httpget);


    public interface HttpRequestCallback {
        void httpResponse(HttpResponse httpresponse) throws IOException;
    }
}
