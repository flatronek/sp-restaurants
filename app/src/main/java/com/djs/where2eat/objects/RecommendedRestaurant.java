package com.djs.where2eat.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebo on 2016-06-09.
 */
public class RecommendedRestaurant {
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
    private double recommendation;
    @SerializedName("rate")
    private double overallRate;
    @SerializedName("userRate")
    private Integer userRate;

    public RecommendedRestaurant(int id, String name, String description, String address, String url, double lat, double lng, int recommendation, double overallRate, Integer userRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.url = url;
        this.lat = lat;
        this.lng = lng;
        this.recommendation = recommendation;
        this.overallRate = overallRate;
        this.userRate = userRate;
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

    public double getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
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
