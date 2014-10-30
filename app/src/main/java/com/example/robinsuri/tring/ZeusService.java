package com.example.robinsuri.tring;

/**
 * Created by robinsuri on 10/29/14.
 */
public class ZeusService {
    String getOrCreateUrl = "kujo.app/zeus/1.0/getOrCreateProfile";
    String sessionUrl = "kujo.app/zeus/1.0/mcSessionInitiate";
    String stagingUrl;
    ZeusClient zeusClient = new ZeusClient();


  public void createAccount(String firstName, String lastName, String number, String emailId, final Tring.callbackForCreateAccount testcallback)
  {
      zeusClient.setGetOrCreateUrl(getOrCreateUrl);
      zeusClient.setStagingUrl(stagingUrl);
    zeusClient.createAccount(firstName, lastName, number, emailId, testcallback);
  }
    public void createSession(String guid,Tring.callbackForSessionCreate testcallback)
    {
        zeusClient.setSessionUrl(sessionUrl);
        zeusClient.getSessionMappingFromGuid(guid,testcallback);
    }
    public void setStagingUrl(String stagingUrl) {
        this.stagingUrl = stagingUrl;
    }

    public void getmapping(String firstName, String lastName, String number, String emailId, final Tring.callbackForSessionCreate callbackForSessionCreate) {
        Tring.callbackForCreateAccount callbackforcreateaccount = new Tring.callbackForCreateAccount() {
            @Override
            public void createCallback(String guid) {
               createSession(guid,callbackForSessionCreate);
            }

            @Override
            public void handleError(Exception e, String errorMessage) {

            }
        };
        createAccount(firstName,lastName,number,emailId,callbackforcreateaccount);
    }


}
