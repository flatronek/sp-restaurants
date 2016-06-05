package com.djs.where2eat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djs.where2eat.R;
import com.djs.where2eat.RestaurantManager;
import com.djs.where2eat.objects.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final double CENTER_LAT = 50.061815;
    private static final double CENTER_LNG = 19.937519;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private List<Restaurant> restaurants;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

        setMapFragment();

        return v;
    }

    private void setMapFragment() {
        mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_map_main_container, mapFragment);
        fragmentTransaction.commit();

        Observable.zip(createMapObservable(), RestaurantManager.INSTANCE.getRecommendedRestaurantsObservable(),
                new Func2<GoogleMap, List<Restaurant>, Object>() {
                    @Override
                    public Object call(GoogleMap googleMap, List<Restaurant> restaurants) {
                        MapFragment.this.map = googleMap;
                        MapFragment.this.restaurants = restaurants;

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        setupGoogleMap();
                        setupRestaurantMarkers();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "merged observable error: ", throwable);
                    }
                });
    }

    private Observable<GoogleMap> createMapObservable() {
        return Observable.create(new Observable.OnSubscribe<GoogleMap>() {
            @Override
            public void call(final Subscriber<? super GoogleMap> subscriber) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        subscriber.onNext(googleMap);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    private void setupGoogleMap() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CENTER_LAT, CENTER_LNG), 10));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: " + latLng.latitude + "; " + latLng.longitude);
            }
        });
    }

    private void setupRestaurantMarkers() {
        for (Restaurant restaurant : restaurants) {
            MarkerOptions markerOptions = new MarkerOptions()
                            .title(restaurant.getName())
                            .snippet(restaurant.getDescription())
                            .draggable(false)
                            .position(new LatLng(restaurant.getLat(), restaurant.getLng()));

            map.addMarker(markerOptions);
        }
    }
}
