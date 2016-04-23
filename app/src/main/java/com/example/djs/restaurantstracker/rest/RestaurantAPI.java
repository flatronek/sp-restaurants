package com.example.djs.restaurantstracker.rest;

import com.example.djs.restaurantstracker.objects.Restaurant;

import java.util.List;

import butterknife.Bind;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Sebo on 2016-03-16.
 */
public interface RestaurantAPI {
    String ENDPOINT = "http://restaurants-juraszek.rhcloud.com";

    @GET("/restaurants")
    Observable<List<Restaurant>> getRestaurants();

    @GET("/restaurantsWithUserRate")
    Observable<List<Restaurant>> getRestaurants(@Query("userToken") String token);

    @GET("/restaurants")
    Observable<Restaurant> getRestaurant(@Query("name") String name);

    @POST("/rateRestaurant")
    Observable<Response> rateRestaurant(@Query("userToken") String token, @Query("restaurantID") int restaurantID,
                                        @Query("rate") double rate, @Body String comment);
}
