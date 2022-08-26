package com.example.v2_ltd.api;

import static com.example.v2_ltd.api.ServerConfig.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }

    public static void cancelAll() {
        okHttpClient.dispatcher().cancelAll();
    }
}
