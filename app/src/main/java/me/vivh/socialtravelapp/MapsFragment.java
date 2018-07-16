package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    MapView mMapView;
    private View rootView;
    private final static String KEY_LOCATION = "location";
    // empty constructor
    public MapsFragment () {}
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_maps, parent, false);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }
        catch (InflateException e){
            Log.e("MapsFragment.class", "Inflate exception");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

