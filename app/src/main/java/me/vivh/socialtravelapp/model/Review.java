package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import retrofit2.http.Body;

@ParseClassName("Review")
public class Review extends ParseObject{

    private static final String ATTRACTION_KEY = "attraction";
    private static final String USER_KEY = "username";
    private static final String STAR_KEY = "star";
    private static final String BODY_KEY = "body";

    public void setAttraction(Attraction attraction){
        put(ATTRACTION_KEY, attraction);
    }

    public void setUsername(String username){
        put(USER_KEY, username);
    }

    public void setStars(int stars){
        put(STAR_KEY, stars);
    }

    public void setBody(String body){
        put(BODY_KEY, body);
    }

    public String getBody(){
        return getString(BODY_KEY);
    }

    public int getStars(){
        return getInt(STAR_KEY);
    }

    public String getUsername(){
        return getString(USER_KEY);
    }

    public Attraction getAttraction(){
        return (Attraction) getParseObject(ATTRACTION_KEY);
    }
}
