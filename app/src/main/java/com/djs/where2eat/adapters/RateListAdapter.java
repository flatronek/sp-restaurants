package com.djs.where2eat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.djs.where2eat.R;
import com.djs.where2eat.fragments.RateFragment;
import com.djs.where2eat.objects.Restaurant;

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
    private Context context;

    public RateListAdapter(List<Restaurant> restaurants, RateFragment fragment) {
        this.fragment = fragment;
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_restaurant_list_item, parent, false);
        this.context = parent.getContext();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);

        holder.rating = 0;
        holder.restaurantTitle.setText(restaurant.getName());
        holder.expandedLayout.setVisibility(View.GONE);
        holder.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.mainView.getWindowToken(), 0);

                switchViewType(holder);
                fragment.rateRestaurant(restaurant, holder.rating * 2, holder.commentText.getText().toString());
            }
        });
        setStarsClickListener(holder, restaurant);

        if (restaurant.getUserRate() == null) {
            colorStars(holder, holder.rating);
        } else {
            colorStars(holder, restaurant.getUserRate() / 2);
        }
    }

    private void setStarsClickListener(final ViewHolder holder, final Restaurant restaurant) {
        for (ImageView star : holder.stars) {
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (restaurant.getUserRate() == null) {
                        holder.rating = holder.stars.indexOf(v) + 1;
                        colorStars(holder, holder.rating);

                        switchViewType(holder);
                    }
                }
            });
        }
    }

    private void colorStars(ViewHolder holder, double rating) {
        for (int i = 0; i < holder.stars.size(); i++) {
            holder.stars.get(i).setImageResource((i < rating) ? R.drawable.star : R.drawable.star_outline);
        }
    }

    private void switchViewType(ViewHolder holder) {
        holder.expandedLayout.setVisibility((holder.expandedLayout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
    }

    public void setDataSet(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.notifyDataSetChanged();
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

        View mainView;

        int rating;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mainView = itemView;

            stars = new ArrayList<>();
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_1));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_2));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_3));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_4));
            stars.add((ImageView) itemView.findViewById(R.id.rate_star_5));
        }
    }
}
