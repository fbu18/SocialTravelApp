package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Review;
import me.vivh.socialtravelapp.model.Trip;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mReviews;
    Context context;

    public ReviewAdapter(List<Review> reviews){
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_review, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Review review = mReviews.get(i);

        try{
            viewHolder.tvName.setText(review.getUsername());
            viewHolder.tvBody.setText(review.getBody());
            viewHolder.rbRating.setRating(review.getStars());

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.rbRating)
        RatingBar rbRating;

        public ViewHolder(View itemView){

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
