package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trip;


public class ChatListFragment extends Fragment {
    @BindView(R.id.rvChats) RecyclerView rvChats;
    ArrayList<Trip> trips;
    ChatListAdapter chatListAdapter;
    ParseUser currentUser;
    Context context;

    private ChatListFragment.OnFragmentInteractionListener mListener;

    private Unbinder unbinder;

    private ChatListAdapter.Callback callback;

    public ChatListFragment() {
        // Required empty public constructor
    }

    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);


        setUpFragmentVariables(view);

        trips = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(trips, callback);

        rvChats.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChats.setAdapter(chatListAdapter);

        loadTopTrips();


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatListAdapter.Callback) {
            callback = (ChatListAdapter.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                    chatListAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    public interface OnFragmentInteractionListener {
    }

    private void setUpFragmentVariables(View view){
        context = getActivity();
        currentUser = ParseUser.getCurrentUser();
        unbinder = ButterKnife.bind(this, view);
    }
}
