package me.vivh.socialtravelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Comment;
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
            case 2:
                View milestoneView = inflater.inflate(R.layout.item_milestone, viewGroup, false);
                final ViewHolderMilestone viewHolderMilestone = new ViewHolderMilestone(milestoneView);
                return viewHolderMilestone;

        }
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        switch (viewHolder.getItemViewType()) {
            case 0:
                bindPost(viewHolder, i);
                return;
            case 1:
//                String imageUrl = null;
//                final ViewHolderCheckIn viewHolderCheckIn = (ViewHolderCheckIn) viewHolder;
//                Post checkIn = mPosts.get(i);
//                ParseUser checkUser = checkIn.getUser();
//                ParseFile checkImage = (ParseFile) checkUser.getParseFile("profilePic");
//                imageUrl = (String) checkImage.getUrl();
//                String checkUsername = checkUser.getString("displayName");
//                String checkDesc = checkIn.getDescription();
////                String checkInDate = checkIn.getDate("date").toString();
//                Attraction attraction = checkIn.getLocation();
//                String location = attraction.getName();
//                viewHolderCheckIn.username.setText(checkUsername);
//                viewHolderCheckIn.location.setText(location);
//                Glide.with(context).load(imageUrl)
//                        .apply(RequestOptions.placeholderOf(R.color.placeholderColor).circleCrop())
//                        .into(viewHolderCheckIn.profileImage);
//                ParseGeoPoint point = attraction.getPoint();
//                String lat = Double.toString(point.getLatitude());
//                String lng = Double.toString(point.getLongitude());
//                // Use Google Static Maps API to get an image of the map surrounding the attraction
//                String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=5&scale=1&size=200x200&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C" + lat + "," + lng;
//                Glide.with(context).load(mapUrl).apply(RequestOptions.placeholderOf(R.color.placeholderColor)).into(viewHolderCheckIn.ivMap);
                bindCheckIn(viewHolder, i);
                return;
            case 2:
                bindAward(viewHolder, i);
                }
    }

        @Override
        public int getItemCount () {
            return mPosts.size();
        }


        public class ViewHolderPost extends RecyclerView.ViewHolder {
            CommentAdapter commentAdapter;
            ArrayList<Comment> comments = new ArrayList<>();
            Post post;
            @BindView(R.id.lvCommentsPost)
            ListView lvComments;
            @BindView(R.id.ivPostImage) ImageView imageView;
            @BindView(R.id.tvPostUsername) TextView tvUser;
            @BindView(R.id.tvPostDescription) TextView tvDescription;
            @BindView(R.id.ivPostProfile) ImageView ivProfile;
            @BindView(R.id.tvPostDate) TextView tvDate;

            public ViewHolderPost(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                commentAdapter = new CommentAdapter(comments);
            }
            public void getCommentList() {

                final Comment.Query commentQuery = new Comment.Query();
                commentQuery.whereEqualTo("post", post);
                commentQuery.withPost().withUser();
                commentQuery.findInBackground(new FindCallback<Comment>() {
                    @Override
                    public void done(List<Comment> objects, com.parse.ParseException e) {
                        if(e == null) {
                            comments.clear();
                            comments.addAll(objects);
                            commentAdapter.notifyDataSetChanged();
                        } else e.printStackTrace();
                    }
                });
            }

            public ArrayList<Comment> getComments() {
                return comments;
            }

            public void setPost(Post newPost) {
                post = newPost;
            }
        }

        public class ViewHolderCheckIn extends RecyclerView.ViewHolder {
            CommentAdapter commentAdapter;
            ArrayList<Comment> comments = new ArrayList<>();
            Post post;
            @BindView(R.id.ivCheckInProfile)
            ImageView profileImage;
            @BindView(R.id.tvCheckInUser)
            TextView username;
            @BindView(R.id.tvCheckInLocation)
            TextView location;
            @BindView(R.id.ivMap) ImageView ivMap;
            @BindView(R.id.tvCheckInDate) TextView tvDate;
            @BindView(R.id.lvCommentsCheckIn) ListView lvComments;

            public ViewHolderCheckIn(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                commentAdapter = new CommentAdapter(comments);
            }
            public void getCommentList() {

                final Comment.Query commentQuery = new Comment.Query();
                commentQuery.whereEqualTo("post", post);
                commentQuery.withPost().withUser();
                commentQuery.findInBackground(new FindCallback<Comment>() {
                    @Override
                    public void done(List<Comment> objects, com.parse.ParseException e) {
                        if(e == null) {
                            comments.clear();
                            comments.addAll(objects);
                            commentAdapter.notifyDataSetChanged();
                        } else e.printStackTrace();
                    }
                });
            }

            public ArrayList<Comment> getComments() {
                return comments;
            }

            public void setPost(Post newPost) {
                post = newPost;
            }
        }

        public class ViewHolderMilestone extends RecyclerView.ViewHolder {
            CommentAdapter commentAdapter;
            ArrayList<Comment> comments = new ArrayList<>();
            Post post;
            @BindView(R.id.ivAwardPic) ImageView ivProfile;
            @BindView(R.id.tvAwardUser) TextView tvAwardUsername;
            @BindView(R.id.tvAwardDate) TextView tvAwardDate;
            @BindView(R.id.lvCommentsMilestone) ListView lvComments;

            public ViewHolderMilestone(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                commentAdapter = new CommentAdapter(comments);
                getCommentList();
            }

            public void getCommentList() {

                final Comment.Query commentQuery = new Comment.Query();
                commentQuery.whereEqualTo("post", post);
                commentQuery.withPost().withUser();
                commentQuery.findInBackground(new FindCallback<Comment>() {
                    @Override
                    public void done(List<Comment> objects, com.parse.ParseException e) {
                        if(e == null) {
                            comments.clear();
                            comments.addAll(objects);
                            commentAdapter.notifyDataSetChanged();
                        } else e.printStackTrace();
                    }
                });
            }

            public ArrayList<Comment> getComments() {
                return comments;
            }
            public void setPost(Post newPost) {
                post = newPost;
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
            } else return 2;
        }
        public void bindPost(RecyclerView.ViewHolder viewHolder, int i) {
            ViewHolderPost viewHolderPost = (ViewHolderPost) viewHolder;
            Post post = (Post) mPosts.get(i);
            ArrayList<Comment> comments = viewHolderPost.getComments();
            try {
                ParseUser user = post.getUser();
                String name = user.getString("displayName");
                String description = post.getDescription();
                viewHolderPost.tvUser.setText(name);
                viewHolderPost.tvDescription.setText(description);
                ParseFile profilePic = (ParseFile) post.getParseUser("user").getParseFile("profilePic");
                String profileUrl = (String) profilePic.getUrl();
                String date = post.getDate("date").toString();
                viewHolderPost.tvDate.setText(getRelativeTimeAgo(date));
                Glide.with(context).load(profileUrl).apply(RequestOptions.placeholderOf(R.color.placeholderColor).centerCrop()).into(viewHolderPost.ivProfile);
                CommentAdapter commentAdapter = viewHolderPost.commentAdapter;
                viewHolderPost.lvComments.setAdapter(commentAdapter);
                setListViewHeightBasedOnItems(viewHolderPost.lvComments);
                viewHolderPost.setPost(post);
                viewHolderPost.getCommentList();
                if (post.getImage() != null) {
                    String url = post.getImage().getUrl();
                    Glide.with(context).load(url).apply(
                            RequestOptions.placeholderOf(R.color.placeholderColor)).into(viewHolderPost.imageView);
                } else {
                    viewHolderPost.imageView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void bindCheckIn(RecyclerView.ViewHolder viewHolder, int i) {
            String imageUrl = null;
            final ViewHolderCheckIn viewHolderCheckIn = (ViewHolderCheckIn) viewHolder;
            Post checkIn = mPosts.get(i);

//                String checkDesc = checkIn.getDescription();
            try {
                ParseUser checkUser = checkIn.getUser();
                ParseFile checkImage = (ParseFile) checkUser.getParseFile("profilePic");
                imageUrl = (String) checkImage.getUrl();
                String checkUsername = checkUser.getString("displayName");
                String checkInDate = checkIn.getDate().toString();
                viewHolderCheckIn.tvDate.setText(getRelativeTimeAgo(checkInDate));
                Attraction attraction = checkIn.getLocation();
                String location = attraction.getName();
                viewHolderCheckIn.username.setText(checkUsername);
                viewHolderCheckIn.location.setText(location);
                Glide.with(context).load(imageUrl)
                        .apply(RequestOptions.placeholderOf(R.color.placeholderColor).centerCrop())
                        .into(viewHolderCheckIn.profileImage);
                CommentAdapter commentAdapter = viewHolderCheckIn.commentAdapter;
                viewHolderCheckIn.lvComments.setAdapter(commentAdapter);
                setListViewHeightBasedOnItems(viewHolderCheckIn.lvComments);
                viewHolderCheckIn.setPost(checkIn);
                viewHolderCheckIn.getCommentList();

                ParseGeoPoint point = attraction.getPoint();
                String lat = Double.toString(point.getLatitude());
                String lng = Double.toString(point.getLongitude());
                // Use Google Static Maps API to get an image of the map surrounding the attraction
                String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=12&scale=2&size=1000x2000&maptype=normal&format=png&visual_refresh=true&markers=size:large%7Ccolor:0x5E35B1%7Clabel:%7C" + lat + "," + lng;
                Glide.with(context).load(mapUrl).apply(RequestOptions.placeholderOf(R.color.placeholderColor)).into(viewHolderCheckIn.ivMap);

            } catch (NullPointerException e) {
                Toast.makeText(context, "You forgot a date dummy!", Toast.LENGTH_LONG);
            }
        }

        public void bindAward(RecyclerView.ViewHolder viewHolder, int i) {
            final ViewHolderMilestone viewHolderMilestone = (ViewHolderMilestone) viewHolder;
            Post award = mPosts.get(i);
            ParseUser recipient = award.getParseUser("user");
            String recipientName = recipient.getString("displayName");
            String rProfilePic = recipient.getParseFile("profilePic").getUrl();
            String awardsNum = award.getAward() + " trips";
            CommentAdapter commentAdapter = viewHolderMilestone.commentAdapter;
            viewHolderMilestone.lvComments.setAdapter(commentAdapter);
            setListViewHeightBasedOnItems(viewHolderMilestone.lvComments);
            try {
                viewHolderMilestone.tvAwardUsername.setText(recipientName);
                viewHolderMilestone.tvAwardDate.setText(getRelativeTimeAgo(award.getDate().toString()));
                Glide.with(context).load(rProfilePic).apply(RequestOptions.placeholderOf(R.color.placeholderColor).centerCrop()).into(viewHolderMilestone.ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String date) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(date).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

}