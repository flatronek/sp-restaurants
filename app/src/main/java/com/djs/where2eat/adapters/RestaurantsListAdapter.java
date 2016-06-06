package com.djs.where2eat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djs.where2eat.R;
import com.djs.where2eat.activities.RestaurantDetailsActivity;
import com.djs.where2eat.objects.realm.RealmRestaurant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebo on 2016-03-16.
 */
public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.ViewHolder>  {

    private List<RealmRestaurant> restaurants;
    private Context context;

    public RestaurantsListAdapter(List<RealmRestaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurants_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RealmRestaurant restaurant = restaurants.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantDescription.setText(restaurant.getDescription());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(RestaurantDetailsActivity.buildIntent(restaurant.getId(), context));
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.restaurant_title)
        TextView restaurantName;

        @Bind(R.id.restaurant_description)
        TextView restaurantDescription;

        @Bind(R.id.restaurant_icon)
        ImageView restaurantIcon;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            view = itemView;
        }
    }
}
