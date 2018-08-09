package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Attraction;

public class AttractionListFragment extends Fragment {

    ArrayList<Attraction> attractions;
    RecyclerView rvAttractions;
    AttractionAdapter attractionAdapter;
    private AttractionAdapter.Callback callback;
    AsyncHttpClient client;
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

        loadTopAttractions();
        getConfiguration();

        int numColumns = 2;
        rvAttractions.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvAttractions.setAdapter(attractionAdapter);

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
}
