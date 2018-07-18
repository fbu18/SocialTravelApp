package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.R;
import me.vivh.socialtravelapp.model.Trip;


public class TripDetailFragment extends Fragment {

    @BindView(R.id.ivAttractionPic) ImageView ivAttractionPic;
    Trip trip;
    Context context;
    private Unbinder unbinder;

    private OnFragmentInteractionListener mListener;

    public TripDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        try{
            Glide.with(context)
                    .load(trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl())
                    .into(ivAttractionPic);
        }catch(Exception e){
            e.printStackTrace();
        }

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

    public interface OnFragmentInteractionListener {

    }
}
