package com.example.djs.restaurantstracker;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Sebo on 2016-03-11.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
