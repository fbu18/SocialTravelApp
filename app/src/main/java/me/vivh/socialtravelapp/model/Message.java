package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String PROFILE_PIC_KEY = "profilePic";

    public ParseUser getUser() {
        return getParseUser(USER_ID_KEY);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseFile getProfilePic() {
        return getUser().getParseFile(PROFILE_PIC_KEY);
    }

}