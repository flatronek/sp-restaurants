package com.djs.where2eat;

import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.objects.realm.RealmRestaurant;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Sebo on 2016-06-05.
 */
public enum RestaurantManager {

    INSTANCE;

    private static final String TAG = "RestaurantManager";

    public void getRecommendedRestaurants(RestaurantManagerListener listener) {
        Realm realm = Realm.getDefaultInstance();

        List<Restaurant> result = new ArrayList<>();

        for (RealmRestaurant realmRestaurant : realm.where(RealmRestaurant.class).findAll().subList(0, 10)) {
            result.add(new Restaurant(realmRestaurant));
        }

        listener.onDownloaded(result);
    }

    public interface RestaurantManagerListener {
        void onDownloaded(List<Restaurant> restaurants);
        void onError(String message);
    }
}
