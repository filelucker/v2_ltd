package com.example.v2_ltd.api;

import com.example.v2_ltd.model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetService {

    @GET("getSurvey")
    Call<List<ResponseData>> getSurvey(@Query(value = "timestamp") String timestamp);

}
