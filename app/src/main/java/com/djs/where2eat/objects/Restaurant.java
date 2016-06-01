package com.djs.where2eat.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebo on 2016-03-16.
 */
public class Restaurant {

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

    public Restaurant(int id, String name, String description, String address, String url, double lat, double lng, double overallRate, Integer userRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.url = url;
        this.lat = lat;
        this.lng = lng;
        this.overallRate = overallRate;
        this.userRate = userRate;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

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

    public Integer getUserRate() {
        return userRate;
    }

    public void setUserRate(Integer userRate) {
        this.userRate = userRate;
    }
}
