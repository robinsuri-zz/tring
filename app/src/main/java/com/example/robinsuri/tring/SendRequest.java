package com.example.robinsuri.tring;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * Created by robinsuri on 10/28/14.
 */
public interface SendRequest {
    public void sendPostRequest(HttpPost httpost);
    public void sendGetrequest(HttpGet httpget);
}
