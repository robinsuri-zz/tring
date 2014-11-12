package com.example.robinsuri.tring;

import android.util.Log;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by robinsuri on 10/28/14.
 */
public class SendHttpRequestImpl implements ISendHttpRequest {

    Executor executor = Executors.newSingleThreadExecutor();
    ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

    public ListenableFuture<HttpResponse> sendPostRequest(final HttpPost postRequest) {

        final ListenableFuture<HttpResponse> future = pool.submit(new Callable<HttpResponse>() {
            @Override
            public HttpResponse call() throws Exception {
                final HttpClient httpclient = new DefaultHttpClient();
                Log.d("Tring", "inside sendRequest");
                HttpResponse response = httpclient.execute(postRequest);
                Log.d("Tring", "URI : " + postRequest.getURI());

                return response;
            }
        });

        return future;


    }

    @Override
    public void sendGetrequest(HttpGet httpget) {

    }


}
