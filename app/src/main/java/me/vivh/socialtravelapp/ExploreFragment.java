package me.vivh.socialtravelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Review;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.btnSuggest) Button suggestBtn;
    GeoDataClient mGeoDataClient;
    SupportPlaceAutocompleteFragment placeAutocompleteFragment;

    private OnFragmentInteractionListener mListener;
    Review review;

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

        unbinder = ButterKnife.bind(this, rootView);

        // don't display back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        mGeoDataClient = Places.getGeoDataClient(getActivity());

        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openSuggestion();

            }
        });

        placeAutocompleteFragment =  (SupportPlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText etPlace = (EditText) view.findViewById(R.id.place_autocomplete_search_input);
        etPlace.setHint("I have a destination in mind!");
        etPlace.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        // TODO - place API key in secret.xml
        //uploadYelpAttractions("@string/yelp_api_key");
        try {
            uploadYelpAttractions("H_XUmSP8Cm_SuKapYw6fb_negJU39gbZvPvCoVqtrLfSrfvtjAq-fWIEpfkK89-cpIYhF-3Hu6XccnseGW-GRDwiqcOwbjiCRctJesF4RtJpqUUyIOtEyxVmNv5nW3Yx");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onResume() {
        super.onResume();
        EditText etPlace = (EditText) getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace.setText("");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ((SupportPlaceAutocompleteFragment) childFragment).setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        String description = "One of Seattle's finest gems";
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

                        try{ name = place.getName().toString();}
                        catch (Exception e) { name = "";}

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
                        newAtt.setDescription(description);
                        newAtt.setPoints(1);
                        // set image in the retrieveUploadPhoto method so that upload is only
                        // executed after photo request is complete
                        retrieveUploadPhoto(id, newAtt);
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

    public interface OnFragmentInteractionListener {
        void openSuggestion();
        void openAttractionDetails(Attraction attraction);
    }

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
                                    mListener.openAttractionDetails(attraction);
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


    public List<Attraction> uploadYelpAttractions(String yelpAPIKey) throws IOException {
        List<Attraction> yelpAttractions = new ArrayList<>();
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
//        YelpFusionApi yelpFusionApi = null;
//        try {
            final YelpFusionApi yelpFusionApi = apiFactory.createAPI(yelpAPIKey);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "unique fun attractions");
        params.put("latitude", "47.6062");
        params.put("longitude", "-122.3321");
        params.put("limit","15");

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                final SearchResponse searchResponse = response.body();
                int totalNumberOfResults = searchResponse.getTotal();
                Log.d("AttractionListFragment","totalNumberOfResults = " + totalNumberOfResults);

                ArrayList<Business> businesses = searchResponse.getBusinesses();

                for (int i = 0; i < businesses.size(); i++){

                    String businessId = businesses.get(i).getId();
                    String businessName = businesses.get(i).getName();
                    String businessPhone = businesses.get(i).getDisplayPhone();
                    String businessAddress = businesses.get(i).getLocation().getAddress1() + ", "
                            + businesses.get(i).getLocation().getCity() + ", "
                            + businesses.get(i).getLocation().getState() + " "
                            + businesses.get(i).getLocation().getZipCode() + ", "
                            + businesses.get(i).getLocation().getCountry();
                    Double businessRating = businesses.get(i).getRating();
                    Double businessLatitude = businesses.get(i).getCoordinates().getLatitude();
                    Double businessLongitude = businesses.get(i).getCoordinates().getLongitude();
                    String businessWebsite = businesses.get(i).getUrl();
                    //int businessPriceLevel = businesses.get(i).getPrice();
                    final String businessImageUrl = businesses.get(i).getImageUrl();
                    String businessDescription = businesses.get(i).getCategories().get(0).getTitle();
                    Log.d("ExploreFragment","businessName = " + businessName);
                    Log.d("ExploreFragment","businessDescription = " + businessDescription);

                    final Attraction newAtt = new Attraction(businessId, businessName, businessAddress, businessPhone, businessLatitude, businessLongitude, businessWebsite, businessRating, businessDescription, 10);
                    if (isDuplicateAttraction(newAtt)){
                        Log.d("ExploreFragment", "Skipping " + newAtt.getName() + " (duplicate attraction)");
                        continue;
                    }

                    //find reviews for business
                    Call<Reviews> reviewCall = yelpFusionApi.getBusinessReviews(businessId, "en_US");

                    Callback<Reviews> reviewCallback = new Callback<Reviews>(){
                        @Override
                        public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                            final Reviews reviewResponse = response.body();
                            final int numReviews = reviewResponse.getTotal();
                            ArrayList<com.yelp.fusion.client.models.Review> reviews = reviewResponse.getReviews();

                            for(int i = 0; i < 3; i++){
                                review = new Review();
                                review.setAttraction(newAtt);
                                review.setBody(reviews.get(i).getText());
                                review.setStars(reviews.get(i).getRating());
                                review.setUsername(reviews.get(i).getUser().getName());
                                review.saveInBackground();
                                Log.d("ExploreFragment", "saved reviews");
                            }

                            try{new AsyncGettingBitmapFromUrl(newAtt, review, businessImageUrl).execute();}
                            catch (Exception e) {e.printStackTrace();}
                        }

                        @Override
                        public void onFailure(Call<Reviews> call, Throwable t) {
                            Log.d("ExploreFragment", "did not save reviews");
                        }
                    };
                    reviewCall.enqueue(reviewCallback);
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);
        return yelpAttractions;
    }


    /**     AsyncTAsk for Image Bitmap  */
    private class AsyncGettingBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {
        private Attraction newAtt;
        private String businessUrl;
        private Review review;
        public AsyncGettingBitmapFromUrl(Attraction a, Review review, String url) {
            this.newAtt = a;
            this.review = review;
            this.businessUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(businessUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    newAtt.setBitmap(myBitmap);
                    Log.d("ExploreFragment","set bitmap");

                    newAtt.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Attraction has been successfully created
                                Log.d("ExploreFragment","New attraction added!");
                                newAtt = null;
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                    review.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Log.d("ExploreFragment","New review added!");
                            }else{
                                e.printStackTrace();
                            }
                        }
                    });
                    return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            System.out.println("bitmap" + bitmap);

        }
    }

    public Boolean isDuplicateAttraction(final Attraction attraction){
        List<Attraction> parseAttractions;
        final Attraction.Query attractionsQuery = new Attraction.Query();
        attractionsQuery.whereEqualTo("name",attraction.getName());
        try {
            parseAttractions = attractionsQuery.find();
            if (parseAttractions.isEmpty()){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


}


