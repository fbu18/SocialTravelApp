package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Attraction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttractionListFragment extends Fragment {

    ArrayList<Attraction> attractions;
    RecyclerView rvAttractions;
    AttractionAdapter attractionAdapter;
    private AttractionAdapter.Callback callback;
    AsyncHttpClient client;
    SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttractionListFragment() {
    }

    public static AttractionListFragment newInstance(String param1, String param2) {
        AttractionListFragment fragment = new AttractionListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_attraction_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        client = new AsyncHttpClient();
        attractions = new ArrayList<>();

        attractionAdapter = new AttractionAdapter(attractions, callback);
        rvAttractions = (RecyclerView) rootView.findViewById(R.id.rvAttractions);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        loadTopAttractions();
        loadYelpAttractions("H_XUmSP8Cm_SuKapYw6fb_negJU39gbZvPvCoVqtrLfSrfvtjAq-fWIEpfkK89-cpIYhF-3Hu6XccnseGW-GRDwiqcOwbjiCRctJesF4RtJpqUUyIOtEyxVmNv5nW3Yx");
        getConfiguration();

        int numColumns = 2;
        rvAttractions.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvAttractions.setAdapter(attractionAdapter);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                attractions.clear();
                attractionAdapter.clear();
                loadTopAttractions();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AttractionAdapter.Callback) {
            callback = (AttractionAdapter.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getConfiguration() {
    }

    public void loadTopAttractions(){
        final Attraction.Query attractionsQuery = new Attraction.Query();
        attractionsQuery.getTop().withName();
        attractionsQuery.orderByAscending("points");

        attractionsQuery.findInBackground(new FindCallback<Attraction>() {
            @Override
            public void done(List<Attraction> objects, ParseException e) {
                if (e==null){
                    for (int i = 0; i < objects.size(); i++){
                        Log.d("MainActivity", "Attraction ["+i+"] = "
                                + objects.get(i).getDescription()
                                + "\n name = " + objects.get(i).getName());

                        attractions.add(0,objects.get(i));
                        attractionAdapter.notifyItemInserted(attractions.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {

    }

    public List<Attraction> loadYelpAttractions(String yelpAPIKey){
        List<Attraction> yelpAttractions = new ArrayList<>();
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
        YelpFusionApi yelpFusionApi = null;
        try {
            yelpFusionApi = apiFactory.createAPI(yelpAPIKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "things to do");
        params.put("latitude", "47.6062");
        params.put("longitude", "-122.3321");
        params.put("limit","10");

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);

//        try {
//            Response<SearchResponse> response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                int totalNumberOfResults = searchResponse.getTotal();
                Log.d("AttractionListFragment","totalNumberOfResults = " + totalNumberOfResults);


                ArrayList<Business> businesses = searchResponse.getBusinesses();
                String businessName = businesses.get(0).getName();
                Double rating = businesses.get(0).getRating();

                Log.d("AttractionListFragment","businessName = " + businessName);
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);
        return yelpAttractions;
    }
}
