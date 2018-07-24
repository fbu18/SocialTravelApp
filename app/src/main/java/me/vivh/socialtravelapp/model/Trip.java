package me.vivh.socialtravelapp.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Trip")
public class Trip extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ATTRACTION = "attraction";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public String getName(){
        return getString(KEY_NAME);
    }

    public Date getDate(){
        return getDate(KEY_DATE);
    }

    public String getDateString(){
        Date date = getDate(KEY_DATE);
        String postFormat = "EEE, MMM dd, yyyy h:mm aaa";
        SimpleDateFormat sf = new SimpleDateFormat(postFormat, Locale.ENGLISH);
        sf.setLenient(true);

        return sf.format(date);
    }

    public Attraction getAttraction(){
        return (Attraction) getParseObject(KEY_ATTRACTION);
    }

    public List<ParseUser> getMembers(){
        return getList(KEY_USER);
    }

    public List<String> getMemberNames(){
        final List members = new ArrayList<String>();
        try{
            ParseRelation<ParseUser> relation = getRelation(KEY_USER);
            ParseQuery query = relation.getQuery();
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    for (ParseUser parseUser : parseUsers)
                    {
                        members.add(parseUser.getUsername());
                    }
                    Log.d("relation", members.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return members;
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    public void setDate(Date date){
        put(KEY_DATE, date);
    }

    public void setKeyAttraction(Attraction attraction){
        put(KEY_ATTRACTION, attraction);
    }

    public static class Query extends ParseQuery<Trip> {
        public Query() {
            super(Trip.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withName() {
            include(KEY_NAME);
            return this;
        }

    }

}
