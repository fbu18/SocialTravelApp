package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Trip;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>{

    private List<Trip> mTrips;
    Context context;

    public TripAdapter(List<Trip> trips) {
        mTrips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_trip, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Trip trip = mTrips.get(i);

        viewHolder.tvGroupName.setText(trip.getName());
        viewHolder.tvDate.setText(trip.getDateString());
        viewHolder.tvDescription.setText(trip.getDescription());

        try{
            String url = trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl();
            Glide.with(context).load(url)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)
                                    .circleCrop())
                    .into(viewHolder.ivAttractionPic);
        }catch(ParseException e){
            e.printStackTrace();
        }



    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvGroupName) TextView tvGroupName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.ivAttractionPic) ImageView ivAttractionPic;
        @BindView(R.id.tvDescription) TextView tvDescription;

        public ViewHolder(View itemView){

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public void clear() {
        mTrips.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Trip> list) {
        mTrips.addAll(list);
        notifyDataSetChanged();
    }



}
