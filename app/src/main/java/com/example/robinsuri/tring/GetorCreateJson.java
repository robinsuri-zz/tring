package com.example.robinsuri.tring;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by robinsuri on 10/30/14.
 */
public class GetorCreateJson {
    @SerializedName("name") NameJson namejson;
    List<String> emails;
    List<String> mobiles;

    public NameJson getNamejson() {
        return namejson;
    }

    public void setNamejson(NameJson namejson) {
        this.namejson = namejson;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }
}
