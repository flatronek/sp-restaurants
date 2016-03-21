package com.example.djs.restaurantstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.example.djs.restaurantstracker.R;
import com.example.djs.restaurantstracker.fragments.RestaurantListFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    TextView drawerFirstName;
    TextView drawerLastName;
    ImageView drawerPicture;

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

        showRestaurantListFragment();
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

        if (id == R.id.nav_restaurants) {
            showRestaurantListFragment();
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
                Log.d(TAG, "facebook login success, token: " + token.getToken() + ", uid: " + token.getUserId());
//                switchDrawerViewType(true);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook login error: " + error.getMessage());
            }
        });

    }

    private void switchDrawerViewType(boolean isLoggedIn) {
        if (isLoggedIn) {
            Profile profile = Profile.getCurrentProfile();

            Log.d(TAG, "facebook login success, id: " + profile.getId() + ", name: " + profile.getFirstName() +
                    ", surname: " + profile.getLastName());

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
            drawerPicture.setVisibility(View.GONE);
            drawerLastName.setText("");
            drawerFirstName.setText("Please log in!");
        }
    }

    private void showRestaurantListFragment() {
        RestaurantListFragment fragment = new RestaurantListFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}
