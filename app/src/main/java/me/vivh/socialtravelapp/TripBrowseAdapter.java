package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import me.vivh.socialtravelapp.model.Trip;

/**
 * Created by dmindlin on 7/23/18.
 */

public class TripBrowseAdapter extends RecyclerView.Adapter<TripBrowseAdapter.ViewHolder>{

    private List<Trip> mTrips;
    Context context;
    private Callback callback;

    interface Callback{
        void openTripDetail(@NonNull Trip trip);
    }

    public TripBrowseAdapter(List<Trip> trips, Callback call) {
        mTrips = trips;
        callback = call;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_browse_trip, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callback.openTripDetail(mTrips.get(viewHolder.getAdapterPosition()));
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Trip trip = mTrips.get(i);

        try{
            viewHolder.tvName.setText(trip.getName());
            viewHolder.tvDate.setText(trip.getDateString());

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mTrips.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvBrowseName) TextView tvName;
        @BindView(R.id.tvBrowseDate) TextView tvDate;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
