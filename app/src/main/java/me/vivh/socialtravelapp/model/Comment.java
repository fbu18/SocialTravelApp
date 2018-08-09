package me.vivh.socialtravelapp.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public final String KEY_USER = "user";
    public final String KEY_DATE = "date";
    public final String KEY_POST = "post";
    public final String KEY_BODY = "body";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public Date getDate() {
        return getDate(KEY_DATE);
    }

    public Post getPost() {
        return (Post) get(KEY_POST);
    }

    public String getBody() {
        return getString(KEY_BODY);
    }

    public static class Query extends ParseQuery<Comment> {
        public Query() {
            super(Comment.class);
        }

    }
}
