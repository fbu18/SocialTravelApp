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
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.CheckInPost;
import me.vivh.socialtravelapp.model.Post;

/**
 * Created by dmindlin on 7/26/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Post> mPosts = new ArrayList<>();
    List<CheckInPost> mCheckIns = new ArrayList<>();
    Context context;

    interface Callback{
        void openMemberProfile(ParseUser user);
    }

    private Callback callback;

    public FeedAdapter(List<Post> posts, Callback inputCallback){
        mPosts = posts;
        callback = inputCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case 0:
                View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
                final ViewHolderPost viewHolder = new ViewHolderPost(postView);
                viewHolder.ivProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.openMemberProfile(mPosts.get(viewHolder.getAdapterPosition()).getUser());
                    }
                });

                viewHolder.tvUser.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        callback.openMemberProfile(mPosts.get(viewHolder.getAdapterPosition()).getUser());
                    }
                });
                return viewHolder;
            case 1:
                View checkInView = inflater.inflate(R.layout.item_check_in, viewGroup, false);
                final ViewHolderCheckIn checkInViewholder = new ViewHolderCheckIn(checkInView);
                return checkInViewholder;

        }
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        switch(getItemViewType(i)) {
            case 0:
                ViewHolderPost viewHolderPost = (ViewHolderPost) viewHolder;
                Post post = (Post) mPosts.get(i);
                ParseUser user = post.getUser();
                String name = null;
                try {
                    name = user.fetchIfNeeded().getUsername();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String description = post.getDescription();
                viewHolderPost.tvUser.setText(name);
                viewHolderPost.tvDescription.setText(description);
                ParseFile profilePic = (ParseFile) post.getParseUser("user").get("profilePic");
                String profileUrl = (String) profilePic.getUrl();
                String date = post.getDate("date").toString();
                viewHolderPost.tvDate.setText(date);
                Glide.with(context).load(profileUrl).apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop()).into(viewHolderPost.ivProfile);
                if(post.getImage() != null) {
                    String url = post.getImage().getUrl();
                    Glide.with(context).load(url).apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)).into(viewHolderPost.imageView);
                } else {
                    viewHolderPost.imageView.setVisibility(View.GONE);
                }
            case 1:
                ViewHolderCheckIn viewHolderCheckIn = (ViewHolderCheckIn) viewHolder;
                Post checkIn = mPosts.get(i);
                ParseUser checkUser = checkIn.getUser();
                String imageUrl = (String) checkUser.get("profilePic");
                String checkUsername = checkUser.getUsername();
                String checkDesc = checkIn.getDescription();
                String checkIndate = checkIn.getDate("date").toString();
                Attraction attraction = checkIn.getLocation();
                String location = attraction.getName();
                viewHolderCheckIn.username.setText(checkUsername);
                viewHolderCheckIn.description.setText(checkDesc);
                viewHolderCheckIn.location.setText(location);
                Glide.with(context).load(imageUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop())
                        .into(viewHolderCheckIn.profileImage);

        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public class ViewHolderPost extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPostImage) ImageView imageView;
        @BindView(R.id.tvPostUsername) TextView tvUser;
        @BindView(R.id.tvPostDescription) TextView tvDescription;
        @BindView(R.id.ivPostProfile) ImageView ivProfile;
        @BindView(R.id.tvPostDate) TextView tvDate;

        public ViewHolderPost(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolderCheckIn extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCheckInProfile) ImageView profileImage;
        @BindView(R.id.tvCheckInUser) TextView username;
        @BindView(R.id.tvCheckInLocation) TextView location;
        @BindView(R.id.tvCheckInDesc) TextView description;

        public ViewHolderCheckIn(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        Post post = mPosts.get(position);
        if (post.getType() == "post") {
            return 0;
        } else if (post.getType() == "checkin") {
            return 1;
        } else return 0;
    }
}
