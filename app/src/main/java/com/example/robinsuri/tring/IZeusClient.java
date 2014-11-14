package com.example.robinsuri.tring;

import com.google.common.util.concurrent.ListenableFuture;

import org.apache.http.HttpResponse;

/**
 * Created by robinsuri on 10/30/14.
 */
public interface IZeusClient {
    void setGetOrCreateUrl(String getOrCreateUrl);

    void setSessionUrl(String sessionUrl);

    ListenableFuture<HttpResponse> createAccount(String firstName, String lastName, String number, String emailId);

    ListenableFuture<HttpResponse> getSessionMappingFromGuid(String guid);
}
