package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Trophy;

public class TrophyAdapter extends RecyclerView.Adapter<TrophyAdapter.ViewHolder> {


    private List<Trophy> mTrophies;
    Context context;

    public TrophyAdapter(List<Trophy> trophies) {
        mTrophies = trophies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_trophy, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() { return mTrophies.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTrophyName) TextView tvTrophyName;
        @BindView(R.id.tvTrophyDescription) TextView tvTrophyDesc;
        @BindView(R.id.ivTrophy) ImageView ivTrophy;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trophy trophy = mTrophies.get(position);
        try{
            holder.tvTrophyName.setText(trophy.getName());
            holder.tvTrophyDesc.setText(trophy.getDescription());
            Glide.with(context).load(trophy.getImage().getUrl())
                    .apply(
                            RequestOptions.placeholderOf(R.color.placeholderColor)
                                    .fitCenter()
                                    .transform(new RoundedCornersTransformation(25, 0)))
                    .into(holder.ivTrophy);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTrophies.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Trophy> list) {
        mTrophies.addAll(list);
        notifyDataSetChanged();
    }

}
