package com.djs.where2eat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djs.where2eat.R;
import com.djs.where2eat.objects.RestaurantComment;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sebo on 2016-06-06.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<RestaurantComment> comments;

    public CommentsAdapter(List<RestaurantComment> comments) {
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_comment, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RestaurantComment comment = comments.get(position);

        holder.commentRate.setText(String.valueOf(comment.getRate()));
        holder.commentText.setText(comment.getComment());
        holder.commentUsername.setText(comment.getName());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.restaurant_comment_text)
        TextView commentText;

        @Bind(R.id.restaurant_comment_username)
        TextView commentUsername;

        @Bind(R.id.restaurant_comment_rate)
        TextView commentRate;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
