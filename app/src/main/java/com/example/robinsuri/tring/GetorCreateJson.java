package com.example.robinsuri.tring;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
/**
 * Created by robinsuri on 10/30/14.
 */
public class GetorCreateJson {
    @SerializedName("name") NameJson namejson;
    String emails;
    String mobiles;


    public NameJson getNamejson() {
        return namejson;
    }

    public void setNamejson(NameJson namejson) {
        this.namejson = namejson;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }
}
