package me.vivh.socialtravelapp.model;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

@ParseClassName("Attraction")
public class Attraction extends ParseObject {
    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_RATING = "rating";
    private static final String KEY_POINT = "point";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phoneNumber";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PRICE = "priceLevel";
    private static final String KEY_POINTS = "points";


    public String getId() { return getString(KEY_ID); }
    public void setId(String id) {
        put(KEY_ID, id);
    }

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
    public void setBitmap(Bitmap bitmap) {
        ParseFile photoFile = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);// can use something 70 in case u want to compress the image

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("image_to_be_saved.jpg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    //filed saved successfully :)
                } else {
                    e.printStackTrace();
                }
            }
        });
        put(KEY_IMAGE, photoFile);
    }

    public String getName() { return getString(KEY_NAME); }
    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public Double getRating() {
        return getDouble(KEY_RATING);
    }
    public void setRating(Double rating) {
        if (rating >= 1.0) {
          put(KEY_RATING, rating);
        }
    }

    public ParseGeoPoint getPoint(){
        return getParseGeoPoint(KEY_POINT);
    }
    public void setPoint(Double latitude, Double longitude){
        final ParseGeoPoint loc = new ParseGeoPoint(latitude, longitude);
        put(KEY_POINT, loc);
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }
    public void setAddress(String address) {
        put(KEY_ADDRESS, address);
    }

    public String getWebsite() {
        return getString(KEY_WEBSITE);
    }
    public void setWebsite(String website) {
        put(KEY_WEBSITE, website);
    }

    public String getPhoneNumber() {
        return getString(KEY_PHONE);
    }
    public void setPhoneNumber(String number) { put(KEY_PHONE, number); }

    public String getType() { return getString(KEY_TYPE); }
    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public Integer getPriceLevel() {
        return getInt(KEY_PRICE);
    }
    public void setPriceLevel(Integer priceLevel) {
        if (priceLevel > -1 ) {
          put(KEY_PRICE, priceLevel);
        }
    }

    public int getPoints() {
        if (getNumber(KEY_POINTS) == null){ return 1; }
        else{ return (int) getNumber(KEY_POINTS); }
    }
    public void setPoints(int points) {
        put(KEY_TYPE, points);
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


