package me.vivh.socialtravelapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.vivh.socialtravelapp.model.Attraction;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Attraction.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("travelapp")
                .clientKey("supersecretkey#2")
                .server("https://travelapp-fbu18.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}

