package com.djs.where2eat.deserializers;

import android.util.Log;

import com.djs.where2eat.objects.Restaurant;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebo on 2016-04-25.
 */
public class RestaurantWithUserRateDeserializer implements JsonDeserializer<List<Restaurant>> {

    private static final String TAG = RestaurantWithUserRateDeserializer.class.getSimpleName();

    @Override
    public List<Restaurant> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Restaurant> result = new ArrayList<>();

        for (JsonElement restaurantJson: json.getAsJsonArray()) {
            String name = restaurantJson.getAsJsonObject().get("name").getAsString();
            String address = restaurantJson.getAsJsonObject().get("address").getAsString();
            String description = restaurantJson.getAsJsonObject().get("description").getAsString();
            String url = restaurantJson.getAsJsonObject().get("link").getAsString();
            double latitude = restaurantJson.getAsJsonObject().get("latitude").getAsDouble();
            double longitude = restaurantJson.getAsJsonObject().get("longitude").getAsDouble();
            int id = restaurantJson.getAsJsonObject().get("restaurantID").getAsInt();
            double rate = restaurantJson.getAsJsonObject().get("rate").getAsDouble();

            Integer userRate = null;
            JsonElement userRateElem = restaurantJson.getAsJsonObject().get("userRate");

            if (!userRateElem.isJsonNull()) {
                userRate = userRateElem.getAsInt();
            }

            result.add(new Restaurant(id, name, description, address, url, latitude, longitude, rate, userRate));
        }

        return result;
    }
}
