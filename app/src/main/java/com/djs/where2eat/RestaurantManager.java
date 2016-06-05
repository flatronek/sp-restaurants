package com.djs.where2eat;

import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.objects.realm.RealmRestaurant;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Sebo on 2016-06-05.
 */
public enum RestaurantManager {

    INSTANCE;

    private static final String TAG = "RestaurantManager";

    public Observable<List<Restaurant>> getRecommendedRestaurantsObservable() {
        return Observable.create(new Observable.OnSubscribe<List<Restaurant>>() {
            @Override
            public void call(Subscriber<? super List<Restaurant>> subscriber) {
                Realm realm = Realm.getDefaultInstance();

                List<Restaurant> result = new ArrayList<>();

                for (RealmRestaurant realmRestaurant : realm.where(RealmRestaurant.class).findAll().subList(0, 10)) {
                    result.add(new Restaurant(realmRestaurant));
                }

                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }
}
