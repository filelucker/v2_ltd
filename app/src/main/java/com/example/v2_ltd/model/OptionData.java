package com.example.v2_ltd.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OptionData implements Serializable {

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("referTo")
    @Expose
    private int referTo;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getReferTo() {
        return referTo;
    }

    public void setReferTo(int referTo) {
        this.referTo = referTo;
    }
}
