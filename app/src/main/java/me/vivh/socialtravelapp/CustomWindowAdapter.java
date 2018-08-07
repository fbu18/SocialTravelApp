package me.vivh.socialtravelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
        ImageView image = (ImageView) v.findViewById(R.id.ivInfo);

        TextView description = (TextView) v.findViewById(R.id.tvInfoDescription);
        description.setText(marker.getSnippet());

        ImageView imageView = v.findViewById(R.id.ivInfo);
        if(imageUrl != null)
        Glide.with(context).load(imageUrl)
                .apply(RequestOptions
                        .placeholderOf(R.color.placeholderColor)
                        .circleCrop())
                .into(imageView);
        Button btnGo = v.findViewById(R.id.btnInfo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.openAttractionDetails(attraction);
            }
        });
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}

