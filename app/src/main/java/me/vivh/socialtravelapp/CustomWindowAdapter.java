package me.vivh.socialtravelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import me.vivh.socialtravelapp.model.Attraction;

/**
 * Created by dmindlin on 8/7/18.
 */

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    CustomWindowAdapter.Callback mCallback;
    Context context;
    interface Callback {
        Attraction getDictionary(String key);
        void openAttractionDetails(Attraction attraction);
    }
    LayoutInflater mInflater;
    CustomWindowAdapter(LayoutInflater i, CustomWindowAdapter.Callback callback) {mInflater = i;
        mCallback = callback;}
    @Override
    public View getInfoWindow(final Marker marker) {
        String name = marker.getTitle();
        final Attraction attraction = mCallback.getDictionary(name);
        String imageUrl = attraction.getImage().getUrl();

        View v = mInflater.inflate(R.layout.custom_info_window, null);
        context = v.getContext();
        // Populate fields
        TextView title = (TextView) v.findViewById(R.id.tvInfoName);
        title.setText(name);
        RatingBar rbVoteAverage = v.findViewById(R.id.rbVoteAverage);

        TextView description = (TextView) v.findViewById(R.id.tvInfoDescription);
        description.setText(marker.getSnippet());
        getInfoContents(marker);
        rbVoteAverage.setRating(attraction.getRating().floatValue());
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}

