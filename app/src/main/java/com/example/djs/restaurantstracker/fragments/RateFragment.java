package com.example.djs.restaurantstracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.djs.restaurantstracker.R;
import com.example.djs.restaurantstracker.adapters.RateListAdapter;
import com.example.djs.restaurantstracker.adapters.RestaurantsListAdapter;
import com.example.djs.restaurantstracker.objects.Restaurant;
import com.example.djs.restaurantstracker.rest.RestaurantAPI;
import com.example.djs.restaurantstracker.rest.SimpleRestAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment {

    private static final String TAG = RateFragment.class.getSimpleName();

    @Bind(R.id.rate_restaurants_recycler_view)
    RecyclerView restaurantsRecyclerView;

    @Bind(R.id.rate_restaurants_progress_bar)
    ProgressBar progressBar;

    private List<Restaurant> restaurants;

    private RateListAdapter rateListAdapter;

    public RateFragment() {
        restaurants = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rate, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        downloadRestaurantsList();
    }

    public void rateRestaurant(Restaurant restaurant, int rate) {
        Log.d(TAG, "rateRestaurant() called with: " + "restaurant = [" + restaurant + "], rate = [" + rate + "]");
    }

    private void downloadRestaurantsList() {
        Log.d(TAG, "downloadRestaurantsList: ");
        switchViewType(true);

        SimpleRestAdapter restAdapter = new SimpleRestAdapter();
        RestaurantAPI restaurantAPI = restAdapter.getRestAdapter().create(RestaurantAPI.class);

        restaurantAPI.getRestaurants()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        switchViewType(false);
                        rateListAdapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new Action1<List<Restaurant>>() {
                    @Override
                    public void call(List<Restaurant> restaurants) {
                        Log.d(TAG, "restaurants downloaded: " + restaurants.size());
                        RateFragment.this.restaurants.addAll(restaurants);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "restaurants download error: " + throwable.getMessage());
                    }
                });
    }

    private void initRestaurantsView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rateListAdapter = new RateListAdapter(restaurants, this);

        restaurantsRecyclerView.setLayoutManager(layoutManager);
        restaurantsRecyclerView.setAdapter(rateListAdapter);
        restaurantsRecyclerView.setHasFixedSize(true);
    }

    private void switchViewType(boolean showProgressBar) {
        restaurantsRecyclerView.setVisibility(showProgressBar ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }
}
