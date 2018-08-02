package me.vivh.socialtravelapp.model;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;

/**
 * Created by dmindlin on 7/26/18.
 */

@ParseClassName("Post")
public class Post extends ParseObject {
    private static String KEY_DESCRIPTION = "description";
    private static String KEY_IMAGE = "image";
    private static String KEY_USER = "user";
    private static String KEY_TYPE = "type";
    private static String KEY_LOCATION = "location";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public String getType() {
        return this.getString(KEY_TYPE);
    }

    public Attraction getLocation() {
        return (Attraction) this.get(KEY_LOCATION);
    }

    public void setDescription(String description) {
        this.put(KEY_DESCRIPTION, description);
    }

    public void setImage(ParseFile image) {
        this.put(KEY_IMAGE, image);
    }

    public void setUser(Parse user) {
        this.put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

    }
}