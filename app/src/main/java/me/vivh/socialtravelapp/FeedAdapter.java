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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Post;

/**
 * Created by dmindlin on 7/26/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    List<Post> mPosts = new ArrayList<>();
    Context context;
    public FeedAdapter(List<Post> posts){
        mPosts = posts;
    }
    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);


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
        ParseFile profilePic = (ParseFile) post.getParseUser("user").get("profilePic");
        String profileUrl = (String) profilePic.getUrl();
        Glide.with(context).load(profileUrl).apply(RequestOptions.placeholderOf(R.drawable.background_gradient).circleCrop()).into(viewHolder.ivProfile);
        if(post.getImage() != null) {
            String url = post.getImage().getUrl();
            Glide.with(context).load(url).apply(
                    RequestOptions.placeholderOf(R.drawable.background_gradient)).into(viewHolder.imageView);
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
        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
