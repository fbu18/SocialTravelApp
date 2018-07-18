package me.vivh.socialtravelapp.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Attraction")
public class Attraction extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_RATING = "rating";
    private static final String KEY_POINT = "point";
    private static final String KEY_ADDRESS = "address";

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

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public Double getRating() {
        return getDouble(KEY_RATING);
    }

    public void setRating(Double rating) {
        put(KEY_RATING, rating);
    }

    public ParseGeoPoint getPoint(){
        return getParseGeoPoint(KEY_POINT);
    }

    public void setPoint(Double latitude, Double longitude){
        final ParseGeoPoint loc = new ParseGeoPoint(latitude, longitude);
        put(KEY_POINT, loc);
    }

    public Double getLatitude() {
        return getPoint().getLatitude();
    }


    public Double getLongitude() {
        return getPoint().getLongitude();
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }


    public static class Query extends ParseQuery<Attraction> {
        public Query() {
            super(Attraction.class);
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


