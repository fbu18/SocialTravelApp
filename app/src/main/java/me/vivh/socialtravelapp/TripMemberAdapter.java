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
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TripMemberAdapter extends RecyclerView.Adapter<TripMemberAdapter.ViewHolder> {

    interface CallbackMember{
        void openMemberDetail(@NonNull ParseUser user);
    }

    private TripMemberAdapter.CallbackMember inputCallback;
    private List<ParseUser> mMembers;
    private List<ParseUser> mMembersCheckedIn;
    Context context;
    ParseUser user;

    public TripMemberAdapter(List<ParseUser> members, List<ParseUser> membersCheckedIn, TripMemberAdapter.CallbackMember callback) {
        mMembers = members;
        mMembersCheckedIn = membersCheckedIn;
        inputCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_trip_member, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputCallback.openMemberDetail(user);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ParseUser member = mMembers.get(i);

        viewHolder.tvUsername.setText(member.getUsername());

        boolean isCheckedIn = false;
        for (ParseUser memberCheckedIn : mMembersCheckedIn){
            if (memberCheckedIn.getUsername().equals(member.getUsername())){
                isCheckedIn = true;
            }
        }
        if (isCheckedIn) { viewHolder.ivCheckedIn.setVisibility(View.VISIBLE); }
        else { viewHolder.ivCheckedIn.setVisibility(View.GONE); }

        String profilePicUrl = "";
        if (member.getParseFile("profilePic") != null) {
            profilePicUrl = member.getParseFile("profilePic").getUrl();
        }
        Glide.with(context).load(profilePicUrl)
                .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_perm_identity_black_24dp)
                                .transform(new RoundedCornersTransformation(25, 2)))
                .into(viewHolder.ivProfilePic);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivProfilePic) ImageView ivProfilePic;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvHomeLoc) TextView tvHomeLoc;
        @BindView(R.id.ivCheckedIn) ImageView ivCheckedIn;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    public void clear() {
        mMembers.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ParseUser> list) {
        mMembers.addAll(list);
        notifyDataSetChanged();
    }


}
