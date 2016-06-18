package com.djs.where2eat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.djs.where2eat.R;
import com.djs.where2eat.adapters.RecommendedRestaurantAdapter;
import com.djs.where2eat.objects.RecommendedRestaurant;
import com.djs.where2eat.rest.RestaurantAPI;
import com.djs.where2eat.rest.SimpleRestAdapter;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;


public class RecommendedRestaurantsFragment extends Fragment {

    private static final String TAG = RecommendedRestaurantsFragment.class.getSimpleName();

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.restaurants_recycler_view)
    RecyclerView restaurantsRecyclerView;

    private List<RecommendedRestaurant> restaurants;
    private RecommendedRestaurantAdapter restaurantsListAdapter;

    public RecommendedRestaurantsFragment() {
        restaurants = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recommended_restaurants, container, false);
        ButterKnife.bind(this, v);

        initRestaurantsList();
        initRestaurantsView();

        return v;
    }

    private void initRestaurantsView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        restaurantsListAdapter = new RecommendedRestaurantAdapter(restaurants, getContext());

        restaurantsRecyclerView.setLayoutManager(layoutManager);
        restaurantsRecyclerView.setAdapter(restaurantsListAdapter);
        restaurantsRecyclerView.setHasFixedSize(true);
    }

    private void switchViewType(boolean downloadInProgress) {
        if (downloadInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            restaurantsRecyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            restaurantsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void initRestaurantsList() {
        Log.d(TAG, "initRestaurantsList: profileid: " + Profile.getCurrentProfile().getId());
        SimpleRestAdapter restAdapter = new SimpleRestAdapter();
        RestaurantAPI restaurantAPI = restAdapter.getRestAdapter().create(RestaurantAPI.class);

        restaurantAPI.getRestaurantsWithRecommendation(Profile.getCurrentProfile().getId())
                .timeout(150, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        switchViewType(true);
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        switchViewType(false);
                    }
                })
                .subscribe(new Action1<List<RecommendedRestaurant>>() {
                    @Override
                    public void call(List<RecommendedRestaurant> restaurants) {
                        RecommendedRestaurantsFragment.this.restaurants.addAll(restaurants);
                        RecommendedRestaurantsFragment.this.restaurantsListAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "error downloading restaurants: ", throwable);
                    }
                });
    }
}
