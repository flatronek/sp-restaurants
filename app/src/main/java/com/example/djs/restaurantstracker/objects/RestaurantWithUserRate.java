package com.example.djs.restaurantstracker.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebo on 2016-04-25.
 */
public class RestaurantWithUserRate {
    @SerializedName("restaurantID")
    private int id;
    private String name;
    private String description;
    private String address;
    @SerializedName("link")
    private String url;
    @SerializedName("latitude")
    private double lat;
    @SerializedName("longitude")
    private double lng;
    @SerializedName("rate")
    private double overallRate;
    @SerializedName("userRate")
    private Integer userRate;

    public RestaurantWithUserRate(Integer userRate, double rate, int restaurantID, double latitude, double longitude, String link, String description, String address, String name) {
        this.userRate = userRate;
        this.overallRate = rate;
        this.id = restaurantID;
        this.lat = latitude;
        this.lng = longitude;
        this.url = link;
        this.description = description;
        this.address = address;
        this.name = name;
    }

//    public RestaurantWithUserRate(int id, String name, String description, String address, String url, double lat, double lng, double overallRate, int userRate) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.address = address;
//        this.url = url;
//        this.lat = lat;
//        this.lng = lng;
//        this.overallRate = overallRate;
//        this.userRate = userRate;
//    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getOverallRate() {
        return overallRate;
    }

    public void setOverallRate(double overallRate) {
        this.overallRate = overallRate;
    }

    public int getUserRate() {
        return userRate;
    }

    public void setUserRate(int userRate) {
        this.userRate = userRate;
    }
}
