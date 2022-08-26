package com.example.v2_ltd.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseData implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("required")
    @Expose
    private boolean required;

    @SerializedName("options")
    @Expose
    private List<OptionData> options = null;

    @SerializedName("referTo")
    @Expose
    private String referTo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<OptionData> getOptionsList() {
        return options;
    }

    public void setOptionsList(List<OptionData> optionsList) {
        this.options = optionsList;
    }

    public String getReferTo() {
        return referTo;
    }

    public void setReferTo(String referTo) {
        this.referTo = referTo;
    }
}
