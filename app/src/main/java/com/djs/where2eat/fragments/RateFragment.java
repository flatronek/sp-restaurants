package com.djs.where2eat.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.djs.where2eat.R;
import com.djs.where2eat.adapters.RateListAdapter;
import com.djs.where2eat.deserializers.RestaurantWithUserRateDeserializer;
import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.rest.RestaurantAPI;
import com.djs.where2eat.rest.SimpleRestAdapter;
import com.facebook.Profile;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Observable;
import rx.Subscriber;
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

    @Bind(R.id.rate_search_text)
    SearchView searchText;

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

        initRestaurantsView();
        initSearchView();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        downloadRestaurantsList();
    }

    public void rateRestaurant(final Restaurant restaurant, final int rating, final String comment) {
        final SimpleRestAdapter adapter = new SimpleRestAdapter();
        RestaurantAPI restaurantAPI = adapter.getRestAdapter().create(RestaurantAPI.class);

        restaurantAPI.rateRestaurant(Profile.getCurrentProfile().getId(), restaurant.getId(),
                rating, (!comment.isEmpty()) ? comment : null)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        rateListAdapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        String body = new String(((TypedByteArray) response.getBody()).getBytes());

                        restaurant.setUserRate(rating);
                        Toast.makeText(getContext(), "Thank you for rating!", Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RetrofitError) {
                            RetrofitError error = (RetrofitError) throwable;
                        }

                        Toast.makeText(getContext(), "An error has occured, your rating has not been saved.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void downloadRestaurantsList() {
        switchViewType(true);

        Type collectionType = new TypeToken<List<Restaurant>>() {
        }.getType();

        SimpleRestAdapter restAdapter = new SimpleRestAdapter(collectionType, new RestaurantWithUserRateDeserializer());
        RestaurantAPI restaurantAPI = restAdapter.getRestAdapter().create(RestaurantAPI.class);

        restaurantAPI.getRestaurantsWithUserRate(Profile.getCurrentProfile().getId())
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
                    public void call(List<Restaurant> restaurantWithUserRates) {
                        RateFragment.this.restaurants.addAll(restaurantWithUserRates);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
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

    private void initSearchView() {
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });

        createSearchTextObservable().debounce(150, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!s.isEmpty()) {
                                    List<Restaurant> filteredRestaurants = filterOffers(s);
                                    rateListAdapter.setDataSet(filteredRestaurants);
                                } else {
                                    rateListAdapter.setDataSet(restaurants);
                                }
                            }
                        });
                    }
                });
    }

    private List<Restaurant> filterOffers(String textQuery) {
        List<Restaurant> result = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().toLowerCase().contains(textQuery.toLowerCase())
                    || restaurant.getDescription().toLowerCase().contains(textQuery.toLowerCase())) {

                result.add(restaurant);
            }
        }

        return result;
    }

    private Observable<String> createSearchTextObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.d(TAG, "onQueryTextSubmit: ");
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.d(TAG, "onQueryTextChange: ");
                        if (subscriber.isUnsubscribed()) {
                            searchText.setOnQueryTextListener(null);
                        } else {
                            subscriber.onNext(newText);
                        }
                        return true;
                    }
                };

                searchText.setOnQueryTextListener(listener);
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }
}
