package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Trip")
public class Trip extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ATTRACTION = "attraction";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public String getName(){
        return getString(KEY_NAME);
    }

    public Date getDate(){
        return getDate(KEY_DATE);
    }

    public Attraction getAttraction(){
        return (Attraction) get(KEY_ATTRACTION);
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
