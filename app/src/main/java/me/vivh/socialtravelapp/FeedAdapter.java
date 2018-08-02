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
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Post;

/**
 * Created by dmindlin on 7/26/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Post> mPosts = new ArrayList<>();
    Context context;

    interface Callback {
        void openMemberProfile(ParseUser user);
    }

    private Callback callback;

    public FeedAdapter(List<Post> posts, Callback inputCallback) {
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

                viewHolder.tvUser.setOnClickListener(new View.OnClickListener() {
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

        switch (viewHolder.getItemViewType()) {
            case 0:
                ViewHolderPost viewHolderPost = (ViewHolderPost) viewHolder;
                Post post = (Post) mPosts.get(i);
                ParseUser user = post.getUser();
                String name = user.getUsername();
                String description = post.getDescription();
                viewHolderPost.tvUser.setText(name);
                viewHolderPost.tvDescription.setText(description);
                ParseFile profilePic = (ParseFile) post.getParseUser("user").getParseFile("profilePic");
                String profileUrl = (String) profilePic.getUrl();
                String date = post.getDate("date").toString();
                viewHolderPost.tvDate.setText(date);
                Glide.with(context).load(profileUrl).apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop()).into(viewHolderPost.ivProfile);
                if (post.getImage() != null) {
                    String url = post.getImage().getUrl();
                    Glide.with(context).load(url).apply(
                            RequestOptions.placeholderOf(R.drawable.background_gradient)).into(viewHolderPost.imageView);
                } else {
                    viewHolderPost.imageView.setVisibility(View.GONE);
                }
                return;
            case 1:
                String imageUrl = null;
                final ViewHolderCheckIn viewHolderCheckIn = (ViewHolderCheckIn) viewHolder;
                Post checkIn = mPosts.get(i);
                ParseUser checkUser = checkIn.getUser();
                ParseFile checkImage = (ParseFile) checkUser.getParseFile("profilePic");
                imageUrl = (String) checkImage.getUrl();
                String checkUsername = checkUser.getUsername();
                String checkDesc = checkIn.getDescription();
                String checkInDate = checkIn.getDate("date").toString();
                Attraction attraction = checkIn.getLocation();
                String location = attraction.getName();
                viewHolderCheckIn.username.setText(checkUsername);
                viewHolderCheckIn.description.setText(checkDesc);
                viewHolderCheckIn.location.setText(location);
                Glide.with(context).load(imageUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop())
                        .into(viewHolderCheckIn.profileImage);
                ParseGeoPoint point = attraction.getPoint();
                String lat = Double.toString(point.getLatitude());
                String lng = Double.toString(point.getLongitude());
                // Use Google Static Maps API to get an image of the map surrounding the attraction
                String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&scale=1&size=200x200&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C" + lat + "," + lng;
                Glide.with(context).load(mapUrl).apply(RequestOptions.placeholderOf(R.drawable.background_gradient)).into(viewHolderCheckIn.ivMap);
        }
    }

        @Override
        public int getItemCount () {
            return mPosts.size();
        }


        public class ViewHolderPost extends RecyclerView.ViewHolder {
            @BindView(R.id.ivPostImage)
            ImageView imageView;
            @BindView(R.id.tvPostUsername)
            TextView tvUser;
            @BindView(R.id.tvPostDescription)
            TextView tvDescription;
            @BindView(R.id.ivPostProfile)
            ImageView ivProfile;
            @BindView(R.id.tvPostDate)
            TextView tvDate;

            public ViewHolderPost(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class ViewHolderCheckIn extends RecyclerView.ViewHolder {
            @BindView(R.id.ivCheckInProfile)
            ImageView profileImage;
            @BindView(R.id.tvCheckInUser)
            TextView username;
            @BindView(R.id.tvCheckInLocation)
            TextView location;
            @BindView(R.id.tvCheckInDesc)
            TextView description;
            @BindView(R.id.ivMap) ImageView ivMap;

            public ViewHolderCheckIn(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
        @Override
        public int getItemViewType ( int position){
            Post post = mPosts.get(position);
            String type = post.getType();
            if (type.equals("post")) {
                return 0;
            } else if (type.equals("checkin")) {
                return 1;
            } else return 0;
        }

    }