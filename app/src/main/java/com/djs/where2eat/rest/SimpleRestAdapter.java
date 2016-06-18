package com.djs.where2eat.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.jakewharton.retrofit.Ok3Client;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Sebo on 2016-03-16.
 */
public class SimpleRestAdapter {

    private static final String TAG = SimpleRestAdapter.class.getSimpleName();

    private RestAdapter restAdapter;

    public SimpleRestAdapter() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(RestaurantAPI.ENDPOINT)
                .setClient(new Ok3Client(okHttpClient))
//                .setRequestInterceptor(createRequestInterceptor())
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public SimpleRestAdapter(Type collectionType, JsonDeserializer deserializer) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(collectionType, deserializer);
        Gson gson = gsonBuilder.create();

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(RestaurantAPI.ENDPOINT)
                .setRequestInterceptor(createRequestInterceptor())
                .setConverter(new GsonConverter(gson))
                .build();
    }

    private RequestInterceptor createRequestInterceptor() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
            }
        };

        return requestInterceptor;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }
}
