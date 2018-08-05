package me.vivh.socialtravelapp.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.parse.Parse.getApplicationContext;

@ParseClassName("Trip")
public class Trip extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ATTRACTION = "attraction";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER = "user";
    private static final String KEY_CHECK_IN = "usersCheckedIn";
    public static final String KEY_LAST_MSG = "lastMessage";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getName(){
        return getString(KEY_NAME);
    }
    public void setName(String name){
        put(KEY_NAME, name);
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

    public void setDate(Date date){
        put(KEY_DATE, date);
    }

    public void setKeyAttraction(Attraction attraction){
        put(KEY_ATTRACTION, attraction);
    }

    public ParseRelation<ParseUser> getCheckIn(){ return getRelation(KEY_CHECK_IN); }
    public void setCheckIn(final ParseUser user){
        ParseRelation<ParseUser> membersCheckedIn = getCheckIn();
        membersCheckedIn.add(user);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Trip", "CHECKED IN");
                // TODO- add marker to TripMemberFragment
            }
        });

        //Add number of points corresponding to attraction
        final int attractionPoints = getAttraction().getPoints();
        int updatedPoints = ((int) user.get("points")) + attractionPoints;
        user.put("points",updatedPoints);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String pointsText; //whether or not the word is plural
                if (attractionPoints == 1) { pointsText = " point."; }
                else { pointsText = " points."; }
                Toast.makeText(getApplicationContext(),"Checked in! You have earned " + attractionPoints + pointsText,Toast.LENGTH_LONG ).show();
            }
        });
    }
    public void removeCheckIn(final ParseUser user){
        ParseRelation<ParseUser> membersCheckedIn = getCheckIn();
        membersCheckedIn.remove(user);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Trip", "CHECKED IN");
                // TODO- add marker to TripMemberFragment
            }
        });

        //Remove number of points corresponding to attraction
        final int attractionPoints = getAttraction().getPoints();
        int updatedPoints = ((int) user.get("points")) - attractionPoints;
        user.put("points",updatedPoints);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String pointsText; //whether or not the word is plural
                if (attractionPoints == 1) { pointsText = " point."; }
                else { pointsText = " points."; }
                Toast.makeText(getApplicationContext(),"Checked out! You have lost " + attractionPoints + pointsText,Toast.LENGTH_LONG ).show();
            }
        });
    }
    public void joinTrip(ParseUser user, final Context context) {
        Trip trip = this;
        ParseRelation relation = trip.getRelation("user");
        relation.add(user);
        trip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(context, "Joined group.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void leaveTrip(ParseUser user, final Context context){
        Trip trip = this;
        ParseRelation relation = trip.getRelation("user");
        relation.remove(user);
        trip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(context, "Left group.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getLastMessageTimestamp() {
        return getLastMessage().getTimestamp();
    }

    public Message getLastMessage() {
        return (Message) getParseObject(KEY_LAST_MSG);
    }
    public void setLastMessage(Message msg) {
        put(KEY_LAST_MSG, msg);
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
