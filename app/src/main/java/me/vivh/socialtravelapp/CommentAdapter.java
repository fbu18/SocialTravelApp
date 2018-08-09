package me.vivh.socialtravelapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

import me.vivh.socialtravelapp.model.Comment;

/**
 * Created by dmindlin on 8/8/18.
 */

public class CommentAdapter extends BaseAdapter{
    ArrayList<Comment> mComments;

    public CommentAdapter(ArrayList<Comment> comments) {
        mComments = comments;
    }
    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Comment getItem(int i) {
        return (Comment) mComments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment, viewGroup, false);
        }
        Comment comment = (Comment) getItem(i);
        ImageView imageView = view.findViewById(R.id.ivProfilePic);
        TextView username = view.findViewById(R.id.tvName);
        TextView commentBody = view.findViewById(R.id.tvComment);
        TextView timeStamp = view.findViewById(R.id.tvTimeStamp);

        ParseUser user = comment.getUser();
        String commentText = comment.getBody();
        String name = user.getUsername();
        Date date = comment.getDate();
        String imageUrl = user.getParseFile("profilePic").getUrl();

        username.setText(name);
        commentBody.setText(commentText);
        timeStamp.setText(date.toString());

        Glide.with(context).load(imageUrl).apply(RequestOptions.centerCropTransform()).into(imageView);

        return view;
    }
}
