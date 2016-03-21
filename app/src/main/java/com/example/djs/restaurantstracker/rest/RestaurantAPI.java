package com.example.djs.restaurantstracker.rest;

import com.example.djs.restaurantstracker.objects.Restaurant;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Sebo on 2016-03-16.
 */
public interface RestaurantAPI {
    String ENDPOINT = "https://sp-restaurants-rest-mocked.herokuapp.com/api";

    @GET("/restaurants")
    Observable<List<Restaurant>> getRestaurants();
}
