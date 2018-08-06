package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LiveQueryException;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.vivh.socialtravelapp.model.Message;
import me.vivh.socialtravelapp.model.Trip;

import static me.vivh.socialtravelapp.model.Message.TRIP_KEY;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    RecyclerView rvChat;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    Trip trip;
    ParseLiveQueryClient parseLiveQueryClient;

    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    EditText etMessage;
    Button btSend;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<ParseUser>  members = new ArrayList<>();

    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 700; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseACL currentUserACL = new ParseACL();
        currentUserACL.setPublicReadAccess(true);
        currentUserACL.setPublicWriteAccess(true);
        currentUser.setACL(currentUserACL);
        currentUser.saveInBackground();


        setupMessagePosting();


        try{
            String tripId = "";
            tripId = getIntent().getExtras().getString("tripId");
            Log.d("ChatActivity", tripId);
            if(!tripId.isEmpty()){
                final Trip.Query tripQuery = new Trip.Query();
                tripQuery.whereEqualTo("objectId", tripId);
                tripQuery.findInBackground(new FindCallback<Trip>(){

                    @Override
                    public void done(List<Trip> objects, ParseException e) {
                        trip = objects.get(0);
                        mAdapter.setTrip(trip);
                        queryMembers();
                        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
                        subscribeToMessages();

                        // Lookup the swipe container view
                        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
                        // Setup refresh listener which triggers new data loading
                        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                refreshMessages();
                            }
                        });
                        // Configure the refreshing colors
                        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                                android.R.color.holo_green_light,
                                android.R.color.holo_orange_light,
                                android.R.color.holo_red_light);

                        refreshMessages();
                    }
                });
            }
        }catch (Exception e){
            Log.d("ChatActivity", "No Intent");
        }


        //backup option if ParseLiveQuery doesn't work
        //myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }


    // for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(ChatActivity.this, userId, mMessages);
        setUpRecyclerView();
        rvChat.setAdapter(mAdapter);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = etMessage.getText().toString();
                Message message = new Message();
                message.setBody(data);
                message.setUser(ParseUser.getCurrentUser());
                message.setTrip(trip);
                trip.setLastMessage(message);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            sendPushNotification(data, ParseUser.getCurrentUser().getString("displayName"));
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                            etMessage.setText(null);
                            refreshMessages();
                        }
                        else{
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void queryMembers(){

        try{
            ParseRelation relation = trip.getRelation("user");
            ParseQuery query = relation.getQuery();

            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    Log.d("relation", "Members" + objects.toString());
                    members.clear();
                    members.addAll(objects);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendPushNotification(String message, String sender){

        String currentUser = ParseUser.getCurrentUser().getUsername();

        for(int i = 0; i < members.size(); i++){
            String otherUser = (members.get(i)).getUsername();
            if(!otherUser.equals(currentUser)){
                HashMap<String, String> payload = new HashMap<>();

                payload.put("receiver", otherUser);
                payload.put("customData", trip.getObjectId());
                payload.put("sender", sender);
                payload.put("message", message);
                ParseCloud.callFunctionInBackground("pushNewMessage", payload);
            }
        }

    }
    // Query messages from Parse so we can load them into the chat adapter
    private void refreshMessages() {

        final ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);

        // Construct query to execute
        ParseQuery<Message> messageQuery = ParseQuery.getQuery(Message.class);
        messageQuery.whereEqualTo(TRIP_KEY, trip);
        // Configure limit and sort order
        messageQuery.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);


        // get the latest 50 messages, order will show up oldest to newest of this group
        messageQuery.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        messageQuery.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                    pb.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    private void setUpRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom != oldBottom) {
                    rvChat.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvChat.smoothScrollToPosition(0);

                        }
                    }, 50);
                }
            }
        });
    }

    private void subscribeToMessages() {
        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        parseQuery.whereEqualTo(TRIP_KEY, trip);

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.ENTER, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery<Message> query, Message object) {
                        mMessages.add(0, object);

                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                Log.d("Messages", "a message has been loaded!");
                                rvChat.scrollToPosition(0);
                            }
                        });
                    }
                });
        subscriptionHandling.handleError(new SubscriptionHandling.HandleErrorCallback<Message>() {
            @Override
            public void onError(ParseQuery<Message> query, LiveQueryException exception) {
                Log.d("Messages", "Callback failed");
            }
        });
    }
}