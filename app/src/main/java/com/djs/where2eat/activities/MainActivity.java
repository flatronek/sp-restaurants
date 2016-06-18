package com.djs.where2eat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.djs.where2eat.R;
import com.djs.where2eat.deserializers.RestaurantWithUserRateDeserializer;
import com.djs.where2eat.fragments.MapFragment;
import com.djs.where2eat.fragments.RateFragment;
import com.djs.where2eat.fragments.RecommendedRestaurantsFragment;
import com.djs.where2eat.fragments.RestaurantListFragment;
import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.objects.realm.RealmRestaurant;
import com.djs.where2eat.rest.RestaurantAPI;
import com.djs.where2eat.rest.SimpleRestAdapter;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final int RESTAURANTS_FRAGMENT_ID = 0;
    private static final int RATE_FRAGMENT_ID = 1;
    private static final int MAP_FRAGMENT_ID = 2;
    private static final int RECOMMENDATIONS_FRAGMENT_ID = 3;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    TextView drawerFirstName;
    TextView drawerLastName;
    ImageView drawerPicture;

    private ProgressDialog dialog;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initDrawerViews();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        initFacebookLogin();

        downloadRestaurants();
    }

    private void initDrawerViews() {
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        drawerFirstName = (TextView) headerView.findViewById(R.id.drawer_header_firstname);
        drawerLastName = (TextView) headerView.findViewById(R.id.drawer_header_lastname);
        drawerPicture = (ImageView) headerView.findViewById(R.id.drawer_header_image);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_restaurants:
                switchToFragment(RESTAURANTS_FRAGMENT_ID, null);
                break;
            case R.id.nav_map:
                switchToFragment(MAP_FRAGMENT_ID, null);
                break;
            case R.id.nav_grades:
                switchToFragment(RATE_FRAGMENT_ID, null);
                break;
            case R.id.nav_recommendations:
                switchToFragment(RECOMMENDATIONS_FRAGMENT_ID, null);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    private void downloadRestaurants() {
        Type collectionType = new TypeToken<List<Restaurant>>() {
        }.getType();

        Log.d(TAG, "downloadRestaurants: user id: " + Profile.getCurrentProfile().getId());
        SimpleRestAdapter restAdapter = new SimpleRestAdapter(collectionType, new RestaurantWithUserRateDeserializer());
        RestaurantAPI restaurantAPI = restAdapter.getRestAdapter().create(RestaurantAPI.class);

        restaurantAPI.getRestaurantsWithUserRate(Profile.getCurrentProfile().getId())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .doOnNext(new Action1<List<Restaurant>>() {
                    @Override
                    public void call(List<Restaurant> restaurants) {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();
                        for (Restaurant restaurant : restaurants) {
                            realm.copyToRealmOrUpdate(new RealmRestaurant(restaurant));
                        }
                        realm.commitTransaction();

                        realm.close();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        dialog.dismiss();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Restaurant>>() {
                    @Override
                    public void call(List<Restaurant> restaurants) {
                        switchToFragment(RESTAURANTS_FRAGMENT_ID, null);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "restaurants download error: ", throwable);
                        finish();
                    }
                });
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Synchronization");
        dialog.setMessage("Downloading restaurants...");
        dialog.show();
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                switchDrawerViewType(currentAccessToken != null);
            }
        };
        accessTokenTracker.startTracking();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = AccessToken.getCurrentAccessToken();
//                switchDrawerViewType(true);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void switchDrawerViewType(boolean isLoggedIn) {
        if (isLoggedIn) {
            Profile profile = Profile.getCurrentProfile();

            drawerFirstName.setText(profile.getFirstName());
            drawerLastName.setText(profile.getLastName());
            drawerPicture.setVisibility(View.VISIBLE);

            Picasso.with(MainActivity.this)
                    .load(profile.getProfilePictureUri(512, 512))
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .resize(128, 128)
                    .into(drawerPicture);
        } else {
            startLoginActivity();

//            drawerPicture.setVisibility(View.GONE);
//            drawerLastName.setText("");
//            drawerFirstName.setText("Please log in!");
        }
    }

    private void switchToFragment(int id, Bundle bundle) {
        Fragment fragment;
        switch (id) {
            case RESTAURANTS_FRAGMENT_ID:
                fragment = new RestaurantListFragment();
                break;
            case RATE_FRAGMENT_ID:
                fragment = new RateFragment();
                break;
            case MAP_FRAGMENT_ID:
                fragment = new MapFragment();
                break;
            case RECOMMENDATIONS_FRAGMENT_ID:
                fragment = new RecommendedRestaurantsFragment();
                break;
            default:
                return;
        }
        fragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}
