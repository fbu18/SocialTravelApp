package me.vivh.socialtravelapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    Button knowBtn;
    Button suggestBtn;

    private OnFragmentInteractionListener mListener;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaceAutocompleteFragment placeAutocompleteFragment  = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        placeAutocompleteFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        String placeDetailsStr = place.getName() + "\n"
                                + place.getId() + "\n"
                                + place.getLatLng().toString() + "\n"
                                + place.getAddress() + "\n"
                                + place.getAttributions();
                        Log.i("OnPlaceSelected", placeDetailsStr);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("OnPlaceSelected", "An error occurred: " + status);
                    }
                }
        );
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_explore,
                container, false);

        knowBtn = (Button) rootView.findViewById(R.id.btnKnow);
        suggestBtn = (Button) rootView.findViewById(R.id.btnSuggest);

        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AttractionFragment nextFrag= new AttractionFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.explore_layout_container, nextFrag,"findThisFragment")
                        .commit();*/
                ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
                vp.setCurrentItem(5, false);
            }
        });
        knowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                ViewPager vp = (ViewPager) getActivity().findViewById(R.id.viewPager);
                vp.setCurrentItem(4, false);
            }
        });
        return rootView;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}

