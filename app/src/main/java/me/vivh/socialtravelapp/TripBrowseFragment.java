package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Trip;


public class TripBrowseFragment extends Fragment {
    List<Trip> trips = new ArrayList<>();
    TripBrowseAdapter tripAdapter;
    Attraction attraction;
    @BindView(R.id.rvBrowse) RecyclerView rvBrowse;
    private Unbinder unbinder;
    public TripBrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_browse, container, false);
        unbinder = ButterKnife.bind(this, view);

        trips = new ArrayList<>();
        tripAdapter = new TripBrowseAdapter(trips);
        rvBrowse.setAdapter(tripAdapter);
        rvBrowse.setLayoutManager(new LinearLayoutManager(getContext()));


        loadTopTrips();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                    tripAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setAttraction(Attraction a) {
        attraction = a;
    }
}
