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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Photo;
import me.vivh.socialtravelapp.model.Trip;


public class TripPhotosFragment extends Fragment {

    private static final String ARG_TRIP = "trip";

    private Trip trip;
    private List<Photo> photos;
    private TripPhotoAdapter tripPhotoAdapter;

    @BindView(R.id.rvPhotos) RecyclerView rvPhotos;
    private Unbinder unbinder;

    public TripPhotosFragment() {
        // Required empty public constructor
    }


    public static TripPhotosFragment newInstance(Trip trip) {
        TripPhotosFragment fragment = new TripPhotosFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRIP, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trip = getArguments().getParcelable(ARG_TRIP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_trip_photos, container, false);
       unbinder = ButterKnife.bind(this, view);

       photos = new ArrayList<>();
       tripPhotoAdapter = new TripPhotoAdapter(photos);

       rvPhotos.setLayoutManager(new LinearLayoutManager(getContext()));
       rvPhotos.setAdapter(tripPhotoAdapter);

       populatePhotos();

       return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void populatePhotos(){
        ParseQuery<Photo> photosQuery = ParseQuery.getQuery("Photo");
        photosQuery.whereEqualTo("trip", trip);
        photosQuery.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> objects, ParseException e) {
                if(e == null){
                    photos.clear();
                    photos.addAll(objects);
                    tripPhotoAdapter.notifyDataSetChanged();
                }else{
                    Log.d("TripPhotosFragment", String.format("%s", objects.size()));
                }
            }
        });

    }
}
