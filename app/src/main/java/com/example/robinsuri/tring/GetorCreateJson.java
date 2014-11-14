package com.example.robinsuri.tring;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by robinsuri on 10/30/14.
 */
public class GetorCreateJson {
    @SerializedName("name")
    NameJson _namejson;
    List<String> emails;
    List<String> mobiles;

    public NameJson get_namejson() {
        return _namejson;
    }

    public void set_namejson(NameJson _namejson) {
        this._namejson = _namejson;
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
