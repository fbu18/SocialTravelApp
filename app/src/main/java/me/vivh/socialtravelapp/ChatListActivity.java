package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.vivh.socialtravelapp.model.Trip;

public class ChatListActivity extends AppCompatActivity {
    @BindView(R.id.rvChats) RecyclerView rvChats;
    ArrayList<Trip> trips;
    TripAdapter tripAdapter;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        trips = new ArrayList<>();
        // initialize the adapter -- movies array cannot be reinitialized after this point
        tripAdapter = new TripAdapter(trips);
        //chatAdapter = new ChatAdapter(chats);

        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(chatAdapter);
        //loadTopTrips();
    }

    public void loadTopTrips(){
        final Trip.Query tripsQuery = new Trip.Query();
        tripsQuery.whereEqualTo("user", ParseUser.getCurrentUser()).include("user");

        tripsQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e==null){
                    trips.clear();
                    trips.addAll(objects);
                    tripAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
