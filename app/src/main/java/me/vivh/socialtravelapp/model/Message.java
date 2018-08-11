package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String USER_KEY = "user";
    public static final String BODY_KEY = "body";
    public static final String PROFILE_PIC_KEY = "profilePic";
    public static final String TRIP_KEY = "trip";
    public static final String IMAGE_KEY = "image";

    public ParseUser getUser() { return getParseUser(USER_KEY); }
    public void setUser(ParseUser parseUser) { put(USER_KEY, parseUser); }

    public String getUserId() { return getUser().getObjectId(); }
    public String getUsername() {
        String name = "";
        try {
            name += getUser().fetchIfNeeded().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return name;
    }
    public String getDisplayName() {
        String name = "";
        try {
            name += getUser().fetchIfNeeded().getString("displayName");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getBody() {
        return getString(BODY_KEY);
    }
    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public Trip getTrip() { return (Trip) getParseObject(TRIP_KEY); }
    public void setTrip(Trip trip) { put(TRIP_KEY, trip); }

    public ParseFile getImage() {
        return getParseFile(IMAGE_KEY);
    }
    public void setImage(ParseFile image) {
        put(IMAGE_KEY, image);
    }

    public String getTimestamp() {
        Date date = getCreatedAt();
        DateFormat df = new SimpleDateFormat("MMM d", Locale.getDefault());
        DateFormat df2 = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());
        String timestamp = df.format(date) + ", " + df2.format(date);
        return timestamp;
    }

    public String getTripId() { return getTrip().getObjectId(); }

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