package com.example.robinsuri.tring;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by robinsuri on 10/28/14.
 */
public class sendPostRequest {

    Executor executor = Executors.newSingleThreadExecutor();
    public void sendRequest(final HttpPost postRequest, final requestCallback callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final HttpClient httpclient = new DefaultHttpClient();
                try {
                    Log.d("Tring","inside sendRequest");
                    HttpResponse response = httpclient.execute(postRequest);
//                    HttpEntity entity = response.getEntity();
//                    String responseString = EntityUtils.toString(entity, "UTF-8");
//                    Log.d("Tring",responseString);
                    callback.getsResponse(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

        public interface requestCallback{
            void getsResponse(HttpResponse httpresponse) throws IOException;
        }
}
