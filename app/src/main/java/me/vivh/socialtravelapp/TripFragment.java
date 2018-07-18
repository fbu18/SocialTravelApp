package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    ArrayList<Trip> trips;
    TripAdapter tripAdapter;

    private OnFragmentInteractionListener mListener;

    private Unbinder unbinder;

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
        tripAdapter = new TripAdapter(trips);

        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTrips.setAdapter(tripAdapter);

        loadTopTrips();
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                    for (int i = 0; i < objects.size(); i++){

                        trips.add(0,objects.get(i));
                        tripAdapter.notifyItemInserted(trips.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {

    }
}

