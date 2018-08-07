package me.vivh.socialtravelapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    }
    CustomWindowAdapter(Callback callback) {

    }
    LayoutInflater mInflater;
    CustomWindowAdapter(LayoutInflater i, CustomWindowAdapter.Callback callback) {mInflater = i;
        mCallback = callback;}
    @Override
    public View getInfoWindow(Marker marker) {
        String name = marker.getTitle();
        Attraction attraction = mCallback.getDictionary(name);
        String imageUrl = attraction.getImage().getUrl();

        View v = mInflater.inflate(R.layout.custom_info_window, null);
        context = v.getContext();
        // Populate fields
        TextView title = (TextView) v.findViewById(R.id.tvInfoName);
        title.setText(name);
        ImageView image = (ImageView) v.findViewById(R.id.ivInfo);

        TextView description = (TextView) v.findViewById(R.id.tvInfoDescription);
        description.setText(marker.getSnippet());

        ImageView imageView = v.findViewById(R.id.ivInfo);
        Glide.with(context).load(imageUrl).apply(RequestOptions.placeholderOf(R.color.placeholderColor).circleCrop()).into(imageView);
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
