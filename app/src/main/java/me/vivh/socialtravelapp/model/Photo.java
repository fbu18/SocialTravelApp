package me.vivh.socialtravelapp.model;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Photo")
public class Photo extends ParseObject {

    private static final String TRIP_KEY = "trip";
    private static final String USER_KEY = "user";
    private static final String IMAGE_KEY = "image";
    private static final String CAPTION_KEY = "caption";


    public Trip getTrip(){
        return (Trip)getParseObject(TRIP_KEY);
    }

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }

    public ParseFile getImage() {
        return getParseFile(IMAGE_KEY);
    }

    public String getCaption(){
        return getString(CAPTION_KEY);
    }

    public void setTrip (Trip trip){
        put(TRIP_KEY, trip);
    }

    public void setUser(ParseUser user){
        put(USER_KEY, user);
    }

    public void setImage(ParseFile image){
        put(IMAGE_KEY, image);
    }

    public void setCaption(String caption){
        put(CAPTION_KEY, caption);
    }
}
