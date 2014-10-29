package com.example.robinsuri.tring;

/**
 * Created by robinsuri on 10/29/14.
 */
public class JsonGenerate {


    public String generateJsonGetOrCreate(String firstName, String lastName, String number, String emailId) {
       return  "{\n" +
                "  \"name\" : {\n" +
                "    \"firstName\" : \"" + firstName + "\",\n" +
                "    \"lastName\" : \"" + lastName + "\"\n" +
                "  },\n" +
                "  \"emails\" : [\"" + emailId + "\"],\n" +
                "  \"mobiles\" : [ \"" + number + "\"]\n" +
                "}";

    }

    public String generateSessionRequest(String guid) {
        return "{\n" +
                "    \"guid\": \"" + guid + "\"\n" +
                "}";
    }
}
