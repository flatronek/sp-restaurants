package com.djs.where2eat;

import android.app.Application;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Sebo on 2016-03-11.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());

        initRealmConfiguration();
    }

    private void initRealmConfiguration() {
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("where2eat.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
    }
}
