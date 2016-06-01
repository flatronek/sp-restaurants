package com.djs.where2eat.objects.realm;

import com.djs.where2eat.objects.Restaurant;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sebo on 2016-06-01.
 */
public class RealmRestaurant extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String address;
    private String url;
    private double lat;
    private double lng;
    private double overallRate;
    private Integer userRate;

    public RealmRestaurant() {
    }

    public RealmRestaurant(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.description = restaurant.getDescription();
        this.address = restaurant.getAddress();
        this.url = restaurant.getUrl();
        this.lat = restaurant.getLat();
        this.lng = restaurant.getLng();
        this.overallRate = restaurant.getOverallRate();
        this.userRate = restaurant.getUserRate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getOverallRate() {
        return overallRate;
    }

    public void setOverallRate(double overallRate) {
        this.overallRate = overallRate;
    }

    public Integer getUserRate() {
        return userRate;
    }

    public void setUserRate(Integer userRate) {
        this.userRate = userRate;
    }
}
