package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trip;

public class TripListFragment extends Fragment {

    @BindView(R.id.rvPastTrips) RecyclerView rvPastTrips;
    @BindView(R.id.rvUpcomingTrips) RecyclerView rvUpcomingTrips;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    ArrayList<Trip> pastTrips;
    TripAdapter pastTripAdapter;
    ArrayList<Trip> upcomingTrips;
    TripAdapter upcomingTripAdapter;
    Calendar cal = new GregorianCalendar();
    Date today;

    private OnFragmentInteractionListener mListener;

    private Unbinder unbinder;

    private TripAdapter.Callback callback;

    public TripListFragment() {
        // Required empty public constructor
    }

    public static TripListFragment newInstance(String param1, String param2) {
        TripListFragment fragment = new TripListFragment();

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
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        unbinder = ButterKnife.bind(this, view);

        // don't display back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        today = cal.getTime();

        pastTrips = new ArrayList<>();
        pastTripAdapter = new TripAdapter(pastTrips, callback);

        upcomingTrips = new ArrayList<>();
        upcomingTripAdapter = new TripAdapter(upcomingTrips, callback);

        rvPastTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPastTrips.setAdapter(pastTripAdapter);

        rvUpcomingTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUpcomingTrips.setAdapter(upcomingTripAdapter);

        loadPastTrips();
        loadUpcomingTrips();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPastTrips();
                loadUpcomingTrips();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripAdapter.Callback) {
            callback = (TripAdapter.Callback) context;
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

    public void loadPastTrips(){

        final Trip.Query tripsQuery = new Trip.Query();
        tripsQuery.whereEqualTo("user", ParseUser.getCurrentUser()).include("user");
        tripsQuery.whereLessThan("date", today);
        tripsQuery.orderByDescending("date");

        tripsQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e==null){
                    pastTrips.clear();
                    pastTrips.addAll(objects);
                    pastTripAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void loadUpcomingTrips(){


        final Trip.Query tripsQuery = new Trip.Query();
        tripsQuery.whereEqualTo("user", ParseUser.getCurrentUser()).include("user");
        tripsQuery.whereGreaterThan("date", today);
        tripsQuery.orderByAscending("date");

        tripsQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e==null){
                    upcomingTrips.clear();
                    upcomingTrips.addAll(objects);
                    upcomingTripAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {

    }
}

