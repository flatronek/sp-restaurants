package com.djs.where2eat.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.djs.where2eat.R;
import com.djs.where2eat.adapters.RestaurantsListAdapter;
import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.objects.realm.RealmRestaurant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantListFragment extends Fragment {

    private static final String TAG = RestaurantListFragment.class.getSimpleName();

    @Bind(R.id.restaurants_progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.restaurants_recycler_view)
    RecyclerView restaurantsRecyclerView;

    private List<RealmRestaurant> restaurants;
    private RestaurantsListAdapter restaurantsListAdapter;

    public RestaurantListFragment() {
        restaurants = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, v);

        initRestaurantsList();
        initRestaurantsView();

        return v;
    }

    private void initRestaurantsView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        restaurantsListAdapter = new RestaurantsListAdapter(restaurants, getContext());

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
        Realm realm = Realm.getDefaultInstance();

        restaurants = realm.copyFromRealm(realm.where(RealmRestaurant.class).findAll());

        realm.close();
    }
}
