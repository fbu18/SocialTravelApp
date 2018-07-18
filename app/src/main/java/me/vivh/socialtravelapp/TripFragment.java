package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Trip;

public class TripFragment extends Fragment {

    @BindView(R.id.rvTrips) RecyclerView rvTrips;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    ArrayList<Trip> trips;
    TripAdapter tripAdapter;

    private OnFragmentInteractionListener mListener;

    private Unbinder unbinder;

    private TripAdapter.Callback callback;

    public TripFragment() {
        // Required empty public constructor
    }

    public static TripFragment newInstance(String param1, String param2) {
        TripFragment fragment = new TripFragment();

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

        trips = new ArrayList<>();
        tripAdapter = new TripAdapter(trips, callback);

        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTrips.setAdapter(tripAdapter);

        loadTopTrips();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopTrips();
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

    public void loadTopTrips(){
        final Trip.Query tripsQuery = new Trip.Query();
        tripsQuery.getTop().withName();

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

    public interface OnFragmentInteractionListener {

    }
}

