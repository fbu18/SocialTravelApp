package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String USER_KEY = "user";
    public static final String BODY_KEY = "body";
    public static final String PROFILE_PIC_KEY = "profilePic";

    public ParseUser getUser() { return getParseUser(USER_KEY); }
    public void setUser(ParseUser parseUser) { put(USER_KEY, parseUser); }

    public String getUserId() { return getUser().getObjectId(); }
    //public void setUserId(String userId) { put(USER_ID_KEY, userId); }

    public String getBody() {
        return getString(BODY_KEY);
    }
    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseFile getProfilePic() {
        ParseFile profilePic = null;
        try {
            profilePic = getUser().fetchIfNeeded().getParseFile(PROFILE_PIC_KEY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return profilePic;
    }

}