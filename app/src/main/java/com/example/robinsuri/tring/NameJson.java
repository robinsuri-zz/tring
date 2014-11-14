package com.example.robinsuri.tring;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robinsuri on 10/30/14.
 */
public class NameJson {
    @SerializedName("firstName")
    String _firstName;
    @SerializedName("lastName")
    String _lastName;

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        this._lastName = lastName;
    }
}
