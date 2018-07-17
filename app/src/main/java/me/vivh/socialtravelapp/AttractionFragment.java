package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import me.vivh.socialtravelapp.dummy.DummyContent.DummyItem;
import me.vivh.socialtravelapp.model.Attraction;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AttractionFragment extends Fragment {

    ArrayList<Attraction> attractions;
    RecyclerView rvAttractions;
    AttractionAdapter adapter;
    AsyncHttpClient client;
    SwipeRefreshLayout swipeContainer;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttractionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_attraction_list, container, false);

        client = new AsyncHttpClient();
        attractions = new ArrayList<>();

        adapter = new AttractionAdapter(attractions);
        rvAttractions = (RecyclerView) rootView.findViewById(R.id.rvAttractions);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        rvAttractions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAttractions.setAdapter(adapter);

        loadTopAttractions();
        getConfiguration();

        int numColumns = 2;
        rvAttractions.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvAttractions.setAdapter(adapter);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                attractions.clear();
                adapter.clear();
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


/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    public void getConfiguration() {
    }

    public void loadTopAttractions(){
        final Attraction.Query attractionsQuery = new Attraction.Query();
        attractionsQuery.getTop().withName();

        attractionsQuery.findInBackground(new FindCallback<Attraction>() {
            @Override
            public void done(List<Attraction> objects, ParseException e) {
                if (e==null){
                    for (int i = 0; i < objects.size(); i++){
                        Log.d("MainActivity", "Attraction ["+i+"] = "
                                + objects.get(i).getDescription()
                                + "\n name = " + objects.get(i).getName());

                        attractions.add(0,objects.get(i));
                        adapter.notifyItemInserted(attractions.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
