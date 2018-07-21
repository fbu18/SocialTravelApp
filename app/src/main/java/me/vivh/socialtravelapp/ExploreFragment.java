package me.vivh.socialtravelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseException;
import com.parse.SaveCallback;

import me.vivh.socialtravelapp.model.Attraction;


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
    GeoDataClient mGeoDataClient;

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
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_explore,
                container, false);

        mGeoDataClient = Places.getGeoDataClient(getActivity());

        knowBtn = (Button) rootView.findViewById(R.id.btnKnow);
        suggestBtn = (Button) rootView.findViewById(R.id.btnSuggest);

        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager vp= (ViewPager) getActivity().findViewById(R.id.viewPager);
                vp.setCurrentItem(4, false);

            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportPlaceAutocompleteFragment placeAutocompleteFragment = new SupportPlaceAutocompleteFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.place_autocomplete_fragment_container, placeAutocompleteFragment)
                .addToBackStack(SupportPlaceAutocompleteFragment.class.getName())
                .commit();
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
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ((SupportPlaceAutocompleteFragment) childFragment).setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: get image and description, convert types to strings?
                        String id = place.getId();
                        String name;
                        String address;
                        String phoneNumber;
                        Double latitude;
                        Double longitude;
                        String website;
                        Double rating;
                        String type;
                        Integer priceLevel;
                        Bitmap bitmap = null;

                        try{ name = place.getName().toString();}
                        catch (Exception e) { name = "";}

                        /*try {
                            getPhotos(id);
                            bitmap = bitmapArray.get(0);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        try{ address = place.getAddress().toString();}
                        catch (Exception e) { address = "42 Wallaby Way, Sydney, Australia";}

                        try{ phoneNumber = place.getPhoneNumber().toString();}
                        catch (Exception e) { phoneNumber = "(650) 867-5309";}

                        try{
                            latitude = place.getLatLng().latitude;
                            longitude = place.getLatLng().longitude;
                        }
                        catch (Exception e) {
                            latitude = 47.6062; //seattle
                            longitude = 122.3321;
                        }

                        try{ website = place.getWebsiteUri().toString();}
                        catch (Exception e) { website = "https://www.facebook.com/";}

                        try{ rating = (double) place.getRating();}
                        catch (Exception e) { rating = 0.0;}

                        try{ type = place.getPlaceTypes().toString();}
                        catch (Exception e) { type = "Fun";}

                        try{ priceLevel = place.getPriceLevel();}
                        catch (Exception e) {  priceLevel = 0;}

                        String placeDetailsStr = name + "\n" + address + "\n" + phoneNumber + "\n"
                                + latitude + ", " + longitude + "," + "\n" + website;
                        Log.i("OnPlaceSelected", placeDetailsStr);


                        Attraction newAtt = new Attraction();
                        newAtt.setId(id);
                        newAtt.setName(name);
                        newAtt.setAddress(address);
                        newAtt.setPhoneNumber(phoneNumber);
                        newAtt.setPoint(latitude, longitude);
                        newAtt.setWebsite(website);
                        newAtt.setRating(rating);
                        newAtt.setType(type);
                        newAtt.setPriceLevel(priceLevel);
                        // set image in the retrieveUploadPhoto method so that upload is only
                        // executed after photo request is complete
                        retrieveUploadPhoto(id, newAtt);
                        // TODO - transition to "join a group" fragment
                    }

                    @Override
                    public void onError(Status status) {
                        Log.i("OnPlaceSelected", "An error occurred: " + status);
                    }
                }
        );
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
     */
    public interface OnFragmentInteractionListener {
    }

    /*public String convertPlaceTypetoString(int value) throws Exception {
        //TODO - implement this if we want to filter by category
        Field[] fields = Place.class.getDeclaredFields();
        String name;
        for (Field field : fields) {
            name = field.getName().toLowerCase();
            if (name.startsWith("type_") && field.getInt(null) == value) {
                return name.replace("type_", "");
            }
        }
        throw new IllegalArgumentException("place value " + value + " not found.");
    }*/


    // Request and upload image to Parse for the specified place.
    // Uploading image is done here so it waits for request to complete.
    private void retrieveUploadPhoto(String placeId, final Attraction attraction) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                // Get the attribution text.
                CharSequence attribution = photoMetadata.getAttributions();
                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();
                        attraction.setBitmap(bitmap);
                        attraction.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Attraction has been successfully created
                                    Toast.makeText( getContext(),"New attraction added!",Toast.LENGTH_LONG ).show();
                                } else {
                                    Toast.makeText( getContext(),"Failed to add new attraction :(",Toast.LENGTH_LONG ).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}


