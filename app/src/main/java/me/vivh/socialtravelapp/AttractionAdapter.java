package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private List<Attraction> mAttractions;
    Context context;
    // pass in attractions
    public void AttractionAdapter(List<Attraction> attractions) {
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
        @BindView(R.id.tvDescription) TextView tvDesc;
        @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
        @BindView(R.id.ivAttrPic) ImageView ivAttrPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvAttrName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // replace existing fragment with profile fragment inside the frame
                    /*ProfileFragment profileFragment = new ProfileFragment();
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentPlace, profileFragment);
                    ft.commit();*/
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
/*        Attraction attraction = mAttractions.get(position);
        String profilePicUrl = "";
        if (post.getUser().getParseFile("profilePic") != null) {
            profilePicUrl = post.getUser().getParseFile("profilePic").getUrl();
        }
        holder.tvDesc.setText(post.getDescription());
        holder.tvUserName.setText(post.getUser().getUsername());
        holder.ivImage.layout(0,0,0,0);
        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivImage);*/
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
