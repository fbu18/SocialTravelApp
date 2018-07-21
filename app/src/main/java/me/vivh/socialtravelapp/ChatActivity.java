package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;

import me.vivh.socialtravelapp.model.Message;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    RecyclerView rvChat;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    EditText etMessage;
    Button btSend;
    private SwipeRefreshLayout swipeContainer;

    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 1000; // milliseconds
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }
        //myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Message>() {
                    @Override
                    public void onEvent(ParseQuery<Message> query, Message object) {
                        mMessages.add(0, object);

                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                rvChat.scrollToPosition(0);
                            }
                        });
                    }
                });

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



    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(ChatActivity.this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setBody(data);
                message.setUser(ParseUser.getCurrentUser());
                //message.setUserId(ParseUser.getCurrentUser().getObjectId());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);


        // get the latest 50 messages, order will show up oldest to newest of this group
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
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
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }


}