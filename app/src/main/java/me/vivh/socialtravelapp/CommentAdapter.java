package me.vivh.socialtravelapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
