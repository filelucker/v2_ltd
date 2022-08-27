package com.example.v2_ltd.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity
public class SaveData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int Id;

    @ColumnInfo(name = "how_are_you")
    private String howAreYou;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "img_data")
    private String imgData;

    @ColumnInfo(name = "what_happned")
    private String whatHappned;

    @ColumnInfo(name = "solve")
    private int solve;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHowAreYou() {
        return howAreYou;
    }

    public void setHowAreYou(String howAreYou) {
        this.howAreYou = howAreYou;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgData() {
        return imgData;
    }

    public void setImgData(String imgData) {
        this.imgData = imgData;
    }

    public String getWhatHappned() {
        return whatHappned;
    }

    public void setWhatHappned(String whatHappned) {
        this.whatHappned = whatHappned;
    }

    public int getSolve() {
        return solve;
    }

    public void setSolve(int solve) {
        this.solve = solve;
    }
}
