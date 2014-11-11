package com.example.robinsuri.tring;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by robinsuri on 10/29/14.
 */
public class ZeusService {

    String getOrCreateUrl = "kujo.app/zeus/1.0/getOrCreateProfile";
    String sessionUrl = "kujo.app/zeus/1.0/mcSessionInitiate";
    String stagingUrl;

    public void setAuthenticateUrl(String authenticateUrl) {
        this.authenticateUrl = authenticateUrl;
    }

    String authenticateUrl = "kujo.app/zeus/1.0/authenticate";

    ZeusClient zeusClient = new ZeusClient();

    public void setSharePreferencesData(SharedPreferences sharePreferencesData) {
        this.sharePreferencesData = sharePreferencesData;
    }

    SharedPreferences sharePreferencesData;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    public void createAccount(String firstName, String lastName, String number, String emailId, final Tring.callbackForCreateAccount testcallback) {
        zeusClient.setGetOrCreateUrl(getOrCreateUrl);
        zeusClient.setStagingUrl(stagingUrl);
        zeusClient.createAccount(firstName, lastName, number, emailId, testcallback);
    }

    public void createSession(String guid, Tring.callbackForSessionCreate testcallback) {
        zeusClient.setSessionUrl(sessionUrl);
        zeusClient.getSessionMappingFromGuid(guid, testcallback);
    }

    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }

    public void getmapping(final String firstName, final String lastName, final String number, final String emailId, final Tring.callbackForSessionCreate callbackForSessionCreate) {
        final Tring.callbackForCreateAccount callbackforcreateaccount = new Tring.callbackForCreateAccount() {
            @Override
            public void createCallback(String guid) {
                persist(guid);
                createSession(guid, callbackForSessionCreate);
            }

            @Override
            public void handleError(Exception e, String errorMessage) {
                callbackForSessionCreate.handleError(e, errorMessage);
            }
        };
        createAccount(firstName, lastName, number, emailId, callbackforcreateaccount);
    }

    private void persist(String guid) {

        SharedPreferences.Editor editor = sharePreferencesData.edit();
        editor.putString("guid", guid);
        editor.commit();
        Log.d("Tring", "Guid from sharedPreferences : " + sharePreferencesData.getString("guid", ""));
    }

    public void getToken(String guid, String sessionId, Tring.callbackForAuthentication callbackForAuthentication) {
        zeusClient.setStagingUrl(stagingUrl);
        zeusClient.setAuthentiateUrl(authenticateUrl);

        zeusClient.authenticate(guid, sessionId, callbackForAuthentication);

    }

}
