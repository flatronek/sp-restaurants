package com.djs.where2eat.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.djs.where2eat.R;
import com.djs.where2eat.adapters.CommentsAdapter;
import com.djs.where2eat.objects.Restaurant;
import com.djs.where2eat.objects.RestaurantComment;
import com.djs.where2eat.objects.realm.RealmRestaurant;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String RESTAURANT_ID = "restaurantId";

    private static final String TAG = RestaurantDetailsActivity.class.getSimpleName();

    @Bind(R.id.restaurant_big_image)
    ImageView restaurantImage;

    @Bind(R.id.restaurant_name)
    TextView restaurantName;

    @Bind(R.id.restaurant_rate)
    TextView restaurantRate;

    @Bind(R.id.restaurant_address)
    TextView restaurantAddress;

    @Bind(R.id.restaurant_phone)
    TextView restaurantPhone;

    @Bind(R.id.restaurant_website)
    TextView restaurantWebsite;

    @Bind(R.id.restaurant_description)
    TextView restaurantDescription;

    @Bind(R.id.restaurant_comments)
    RecyclerView commentsRecyclerView;

    private Restaurant restaurant;
    private List<RestaurantComment> comments;

    private CommentsAdapter commentsAdapter;

    public static Intent buildIntent(int restaurantId, Context context) {
        Bundle extras = new Bundle();
        extras.putInt(RESTAURANT_ID, restaurantId);

        Intent intent = new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        ButterKnife.bind(this);

        int restaurantId = getIntent().getExtras().getInt(RESTAURANT_ID, 0);

        initRestaurantObject(restaurantId);
        initViews();
        downloadComments();
    }

    private void initRestaurantObject(int restaurantId) {
        Realm realm = Realm.getDefaultInstance();

        RealmRestaurant realmRestaurant = realm.where(RealmRestaurant.class).equalTo("id", restaurantId).findFirst();
        if (realmRestaurant != null)
            restaurant = new Restaurant(realmRestaurant);

        realm.close();
    }

    private void initViews() {
        if (restaurant == null)
            return;

        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantDescription.setText(restaurant.getDescription());
        restaurantRate.setText(String.valueOf(restaurant.getOverallRate()));

        SpannableString urlString = new SpannableString(restaurant.getUrl());
        urlString.setSpan(new UnderlineSpan(), 0, urlString.length(), 0);

        restaurantWebsite.setText(urlString);
        restaurantWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String preparedUriString = restaurant.getUrl();
                if (!preparedUriString.startsWith("http://")) {
                    preparedUriString = "http://" + preparedUriString;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(preparedUriString));
                startActivity(browserIntent);
            }
        });
    }

    private void downloadComments() {
        comments = new ArrayList<>();
        comments.add(new RestaurantComment("Sebastian Brandys", "super restauracja polecam 10/10", 4.5));
        comments.add(new RestaurantComment("Sebastian Brandys", "super restauracja polecam 10/10", 4.5));
        comments.add(new RestaurantComment("Sebastian Brandys", "super restauracja polecam 10/10", 4.5));

        commentsAdapter = new CommentsAdapter(comments);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        commentsRecyclerView.setLayoutManager(layoutManager);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }
}
