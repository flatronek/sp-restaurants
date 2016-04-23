package com.example.djs.restaurantstracker.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sebo on 2016-03-16.
 */
public class Restaurant {

    @SerializedName("restaurantId")
    private int id;
    private String name;
    private String description;
    @SerializedName("locationX")
    private double lat;
    @SerializedName("locationY")
    private double lng;

    private int rate;

    public Restaurant(int id, String name, String shortDesc, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.description = shortDesc;
        this.lat = lat;
        this.lng = lng;

        this.rate = 0;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
