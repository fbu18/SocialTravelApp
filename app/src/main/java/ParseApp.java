package me.vivh.SocialTravelApp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.vivh.parstagram.model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("johnny-hopkins")
                .clientKey("sloan-kettering!")
                .server("http://vivh3-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
