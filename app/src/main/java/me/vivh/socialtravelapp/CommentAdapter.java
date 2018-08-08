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

    public CommentAdapter(ArrayList<Comment> comments) {
        
    }
    @Override
    public int getCount() {
        return 0;
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
