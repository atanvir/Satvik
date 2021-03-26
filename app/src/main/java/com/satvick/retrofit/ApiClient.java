package com.satvick.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mobua06 on 16/6/17.
 */

public class ApiClient {
//    public static final String BASE_URL = "https://mobuloustech.com/yodapi/api/";
    public static final String BASE_URL = "http://54.237.193.27/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = (new GsonBuilder()).setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = (new OkHttpClient.Builder()).connectTimeout(80L, TimeUnit.SECONDS).readTimeout(80L, TimeUnit.SECONDS).addInterceptor((Interceptor)httpLoggingInterceptor).build();
        retrofit = (new Retrofit.Builder()).client(okHttpClient).baseUrl(BASE_URL).addConverterFactory((Converter.Factory)GsonConverterFactory.create(gson)).build();
        return retrofit;
    }

    public static Retrofit getClient(String url) {
        Gson gson = (new GsonBuilder()).setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = (new OkHttpClient.Builder()).connectTimeout(80L, TimeUnit.SECONDS).readTimeout(80L, TimeUnit.SECONDS).addInterceptor((Interceptor)httpLoggingInterceptor).build();
        retrofit = (new Retrofit.Builder()).client(okHttpClient).baseUrl(url).addConverterFactory((Converter.Factory)GsonConverterFactory.create(gson)).build();
        return retrofit;
    }


}
