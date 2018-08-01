package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by dmindlin on 7/31/18.
 */

@ParseClassName("CheckIn")
public class CheckInPost extends ParseObject{
    final String KEY_USER = "user";
    final String KEY_DESCRIPTION = "description";
    final String KEY_LOCATION = "location";


    public ParseUser getUser() {
        return this.getParseUser(KEY_USER);
    }

    public String getDescription() {
        return this.getString(KEY_DESCRIPTION);
    }

    public Attraction getLocation() {
        return (Attraction) this.get(KEY_LOCATION);
    }

    public static class Query extends ParseQuery<CheckInPost> {
        public Query() {
            super(CheckInPost.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

    }
}
