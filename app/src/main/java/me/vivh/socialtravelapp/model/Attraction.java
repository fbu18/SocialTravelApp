package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Attraction")
public class Attraction extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_RATING = "rating";
    private static final String KEY_LIKED_BY = "liked_by";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() { return getParseUser(KEY_USER); }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getRating() {
        return getString(KEY_RATING);
    }

    public void setRating(String rating) {
        put(KEY_RATING, rating);
    }

    public String getUsername() {
        return getUser().getUsername();
    }

}

