package com.example.djs.restaurantstracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djs.restaurantstracker.R;
import com.example.djs.restaurantstracker.fragments.RateFragment;
import com.example.djs.restaurantstracker.objects.Restaurant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebo on 2016-04-13.
 */
public class RateListAdapter extends RecyclerView.Adapter<RateListAdapter.ViewHolder> {

    private static final String TAG = RateListAdapter.class.getSimpleName();

    private List<Restaurant> restaurants;
    private RateFragment fragment;

    public RateListAdapter(List<Restaurant> restaurants, RateFragment fragment) {
        this.fragment = fragment;
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_restaurant_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        holder.restaurantTitle.setText(restaurant.getName());
        holder.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchViewType(holder);
                fragment.rateRestaurant(restaurant, restaurant.getRate(), holder.commentText.getText().toString());
            }
        });
        colorStars(holder, restaurant.getRate());

        // prevent rating again
        if (restaurant.getRate() == 0) {
            setStarsClickListener(holder, restaurant);
        }
    }

    private void setStarsClickListener(final ViewHolder holder, final Restaurant restaurant) {
        for (ImageView star : holder.stars) {
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rate = holder.stars.indexOf(v) + 1;

                    restaurant.setRate(rate);
                    colorStars(holder, rate);

                    switchViewType(holder);
                }
            });
        }
    }

    private void colorStars(ViewHolder holder, int rating) {
        for (int i = 0; i < holder.stars.size(); i++) {
            holder.stars.get(i).setImageResource((i < rating) ? R.drawable.star : R.drawable.star_outline);
        }
    }

    private void switchViewType(ViewHolder holder) {
        holder.expandedLayout.setVisibility((holder.expandedLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rate_expanded_layout)
        View expandedLayout;

        @Bind(R.id.rate_list_restaurant_title)
        TextView restaurantTitle;

        @Bind(R.id.rate_list_restaurant_image)
        ImageView restaurantImage;

        @Bind(R.id.rate_button)
        Button rateButton;

        @Bind(R.id.rate_comment_text)
        EditText commentText;

        List<ImageView> stars;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            stars = new ArrayList<>();
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_1));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_2));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_3));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_4));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_5));
        }
    }
}
