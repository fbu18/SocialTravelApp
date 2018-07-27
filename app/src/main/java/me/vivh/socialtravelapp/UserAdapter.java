package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<ParseUser> mUsers;
    Context context;
    boolean displayRank;

    public UserAdapter(List<ParseUser> users, boolean rank) {
        mUsers = users;
        displayRank = rank;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(itemView);
    }

    @Override

    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final ParseUser user = mUsers.get(position);
        if (displayRank) {
            viewHolder.tvRank.setVisibility(View.VISIBLE);
            viewHolder.tvRank.setText(String.valueOf(position + 1));
        }
        else {
            viewHolder.tvRank.setVisibility(View.GONE);
        }

        viewHolder.tvName.setText(user.getString("username"));
        int points = (int) user.getNumber("points");
        viewHolder.tvPoints.setText(String.valueOf(points));
        viewHolder.rlUser.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //TODO -  bring to user profile
            }
        });

        String profilePicUrl = "";
        if (user.getParseFile("profilePic") != null) {
            profilePicUrl = user.getParseFile("profilePic").getUrl();
        }
        Glide.with(getApplicationContext()).load(profilePicUrl)
                .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_perm_identity_black_24dp)
                                .circleCrop())
                .into(viewHolder.ivProfilePic);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRank;
        public TextView tvName;
        public TextView tvPoints;
        public ImageView ivProfilePic;
        public RelativeLayout rlUser;


        public ViewHolder(View itemView) {
            super(itemView);
            tvRank = (TextView) itemView.findViewById(R.id.tvRank);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            rlUser = (RelativeLayout) itemView.findViewById(R.id.rlUser);
        }
    }

    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ParseUser> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }

}

