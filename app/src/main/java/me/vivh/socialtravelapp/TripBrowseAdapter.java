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

import org.w3c.dom.Text;

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


    public TripBrowseAdapter(List<Trip> trips) {
        mTrips = trips;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_browse_trip, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Trip trip = mTrips.get(i);
        viewHolder.tvName.setText(trip.getName());
        viewHolder.tvDate.setText(trip.getDateString());
        viewHolder.tvDesc.setText(trip.getDescription());

        try{
            String url = trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl();
            Glide.with(context).load(url)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)
                                    .circleCrop())
                    .into(viewHolder.imageView);
        }catch(ParseException e){
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
        @BindView(R.id.tvBrowseDesc) TextView tvDesc;
        @BindView(R.id.ivBrowse) ImageView imageView;
        public ViewHolder(View itemView){

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
