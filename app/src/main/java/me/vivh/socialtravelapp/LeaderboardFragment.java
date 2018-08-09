package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardFragment extends Fragment {
    private ArrayList<ParseUser> users;
    private RecyclerView rvUsers;
    private ArrayList<ParseUser> userList;
    private UserAdapter userAdapter;
    private ParseUser currentUser;
    private UserAdapter.Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        rvUsers = (RecyclerView) v.findViewById(R.id.rvLeaderboard);
        users = new ArrayList<>();
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(users, true, callback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setLayoutManager(linearLayoutManager);
        // set the adapter
        rvUsers.setAdapter(userAdapter);
        update();
        return v;
    }



    public void update() {
        userList = loadUsers();
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.orderByAscending("points");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    userAdapter.clear();
                    addItems(userList);
                } else {
                    Log.d("user", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void addItems(List<ParseUser> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ParseUser user= userList.get(i);
            users.add(0, user);
            userAdapter.notifyItemInserted(0);
        }
    }

    public ArrayList<ParseUser> loadUsers(){
        try{
            ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();

            usersQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    try {
                        Log.d("relation", "Members" + objects.toString());
                        userList.clear();
                        userList.addAll(objects);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserAdapter.Callback){
            callback = (UserAdapter.Callback) context;
        }
    }
}

