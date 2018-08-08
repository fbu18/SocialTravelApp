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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Attraction;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    interface Callback{
        void openAttractionDetails(@NonNull Attraction attraction);
        //void openDetail(String TAG, @NonNull Object data);
    }

    private Callback attrCallback;
    private List<Attraction> mAttractions;
    Context context;

    // pass in attractions
    public AttractionAdapter(List<Attraction> attractions, Callback callback) {
        mAttractions = attractions;
        attrCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_attraction_2, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attrCallback.openAttractionDetails(mAttractions.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemCount() { return mAttractions.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAttrName) TextView tvAttrName;
        @BindView(R.id.tvDescription) TextView tvAttrDesc;
        @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
        @BindView(R.id.ivAttrPic) ImageView ivAttrPic;
        @BindView(R.id.tvAttrAddress) TextView tvAttrAddress;
        @BindView(R.id.tvAttrPhoneNumber) TextView tvAttrPhoneNumber;
        @BindView(R.id.tvWebsite) TextView tvWebsite;
        @BindView(R.id.tvPoints) TextView tvPoints;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Attraction attraction = mAttractions.get(position);
        try{
            holder.tvAttrName.setText(attraction.getName());
            holder.tvAttrDesc.setText(attraction.getDescription());
            holder.rbVoteAverage.setNumStars(5);
            holder.rbVoteAverage.setRating(attraction.getRating().floatValue());
            holder.tvAttrAddress.setText(attraction.getAddress());
            holder.tvAttrPhoneNumber.setText(attraction.getPhoneNumber());
            holder.tvWebsite.setText(attraction.getWebsite());
            holder.tvPoints.setText(String.valueOf(attraction.getPoints()));
            Glide.with(context).load(attraction.getImage().getUrl())
                    .apply(
                            RequestOptions.placeholderOf(R.color.placeholderColor)
                                    .fitCenter()
                                    .transform(new RoundedCornersTransformation(25, 0)))
                    .into(holder.ivAttrPic);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mAttractions.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Attraction> list) {
        mAttractions.addAll(list);
        notifyDataSetChanged();
    }

}
