package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private List<Attraction> mAttractions;
    Context context;
    // pass in attractions
    public AttractionAdapter(List<Attraction> attractions) {
        mAttractions = attractions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_attraction, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() { return mAttractions.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAttrName) TextView tvAttrName;
        @BindView(R.id.tvDescription) TextView tvAttrDesc;
        @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
        @BindView(R.id.ivAttrPic) ImageView ivAttrPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvAttrName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO - replace existing fragment with details fragment inside the frame
                    /*AttractionDetailsFragment attractionDetailsFragment = new AttractionDetailsFragment();
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentPlace, attractionDetailsFragment);
                    ft.commit();*/
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attraction attraction = mAttractions.get(position);
        holder.tvAttrName.setText(attraction.getName());
        holder.tvAttrDesc.setText(attraction.getDescription());
        holder.rbVoteAverage.setNumStars((int) Math.round(attraction.getRating()));
        Glide.with(context).load(attraction.getImage().getUrl()).into(holder.ivAttrPic);
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
