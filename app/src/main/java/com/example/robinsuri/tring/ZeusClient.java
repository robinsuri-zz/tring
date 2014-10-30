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
 * Created by robinsuri on 10/29/14.
 */
public class ZeusClient {
    String getOrCreateUrl = "kujo.app/zeus/1.0/getOrCreateProfile";
    String sessionUrl = "kujo.app/zeus/1.0/mcSessionInitiate";
    String stagingUrl;


    public void getMapping(String firstName, String lastName, String number, String emailId, final Tring.TestCallBack testcallback) {
        ZeusService zeusService = new ZeusService();
        zeusService.setStagingUrl(stagingUrl);
        zeusService.setSessionUrl(sessionUrl);
        zeusService.setGetOrCreateUrl(getOrCreateUrl);
        zeusService.getMapping(firstName, lastName, number, emailId, testcallback);

    }


    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }
}
