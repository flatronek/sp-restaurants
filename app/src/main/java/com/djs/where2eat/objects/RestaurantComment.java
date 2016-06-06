package com.djs.where2eat.objects;

/**
 * Created by Sebo on 2016-06-06.
 */
public class RestaurantComment {

    private String name;
    private String comment;
    private double rate;

    public RestaurantComment(String name, String comment, double rate) {
        this.name = name;
        this.comment = comment;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
