package me.vivh.socialtravelapp;

import android.content.Context;
import android.media.Image;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        String name = user.getString("displayName");
        Date date = comment.getDate();
        String relativeDate = getRelativeTimeAgo(date.toString());
        String imageUrl = user.getParseFile("profilePic").getUrl();

        username.setText(name);
        commentBody.setText(commentText);
        timeStamp.setText(relativeDate);

        Glide.with(context).load(imageUrl).apply(RequestOptions.centerCropTransform()).into(imageView);

        return view;
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
}
