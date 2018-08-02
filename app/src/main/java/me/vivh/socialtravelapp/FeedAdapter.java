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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.vivh.socialtravelapp.model.Post;

/**
 * Created by dmindlin on 7/26/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    List<Post> mPosts = new ArrayList<>();
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
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

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
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder viewHolder, int i) {
        Post post = (Post) mPosts.get(i);
        ParseUser user = post.getUser();
        String name = null;
        try {
            name = user.fetchIfNeeded().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String description = post.getDescription();
        viewHolder.tvUser.setText(name);
        viewHolder.tvDescription.setText(description);
        ParseFile profilePic = null;
        try {
            profilePic = (ParseFile) post.getParseUser("user").fetchIfNeeded().get("profilePic");
            String profileUrl = (String) profilePic.getUrl();
            String date = post.getDate("date").toString();
            viewHolder.tvDate.setText(date);
            Glide.with(context).load(profileUrl).apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop()).into(viewHolder.ivProfile);
            if(post.getImage() != null) {
                String url = post.getImage().getUrl();
                Glide.with(context).load(url).apply(
                        RequestOptions.placeholderOf(R.drawable.background_gradient)).into(viewHolder.imageView);
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPostImage) ImageView imageView;
        @BindView(R.id.tvPostUsername) TextView tvUser;
        @BindView(R.id.tvPostDescription) TextView tvDescription;
        @BindView(R.id.ivPostProfile) ImageView ivProfile;
        @BindView(R.id.tvPostDate) TextView tvDate;
        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
