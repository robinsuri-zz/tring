package com.example.robinsuri.tring;

/**
 * Created by robinsuri on 10/30/14.
 */
public interface IZeusClient {
    void setGetOrCreateUrl(String getOrCreateUrl);

    void setSessionUrl(String sessionUrl);

    void setStagingUrl(String stagingUrl);

    void createAccount(String firstName, String lastName, String number, String emailId, Tring.callbackForCreateAccount testcallback);

    void getSessionMappingFromGuid(String guid, Tring.callbackForSessionCreate sessioncallback);
}
