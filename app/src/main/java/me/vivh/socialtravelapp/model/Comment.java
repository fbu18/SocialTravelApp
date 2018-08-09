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
    private static final String KEY_USER = "user";
    private static final String KEY_DATE = "date";
    private static final String KEY_POST = "post";
    private static final String KEY_BODY = "body";

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

        public Query withUser() {
            this.include(KEY_USER);
            return this;
        }

        public Query withPost() {
            this.include(KEY_POST);
            return this;
        }

    }
}
