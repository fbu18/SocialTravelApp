package me.vivh.socialtravelapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Message;
import me.vivh.socialtravelapp.model.Photo;
import me.vivh.socialtravelapp.model.Post;
import me.vivh.socialtravelapp.model.Review;
import me.vivh.socialtravelapp.model.Trip;
import me.vivh.socialtravelapp.model.Trophy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);



        ParseObject.registerSubclass(Attraction.class);
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Photo.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Trophy.class);
        ParseObject.registerSubclass(Review.class);


        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);


        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("travelapp")
                .clientKey("supersecretkey#2")
                .server("https://travelapp-fbu18.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}

