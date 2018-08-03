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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trophy;

public class TrophyListFragment extends Fragment {

    ArrayList<Trophy> trophies;
    RecyclerView rvTrophies;
    TrophyAdapter trophyAdapter;
    AsyncHttpClient client;
    SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrophyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_trophy_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        client = new AsyncHttpClient();
        trophies = new ArrayList<>();

        trophyAdapter = new TrophyAdapter(trophies);
        rvTrophies = (RecyclerView) rootView.findViewById(R.id.rvTrophies);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        loadTrophies();

        int numColumns = 3;
        rvTrophies.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvTrophies.setAdapter(trophyAdapter);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                trophies.clear();
                trophyAdapter.clear();
                loadTrophies();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadTrophies(){
        final Trophy.Query trophyQuery = new Trophy.Query();
        trophyQuery.getTop().withName();
        trophyQuery.orderByDescending("createdAt");

        trophyQuery.findInBackground(new FindCallback<Trophy>() {
            @Override
            public void done(List<Trophy> objects, ParseException e) {
                if (e==null){
                    for (int i = 0; i < objects.size(); i++){
                        Log.d("MainActivity", "Trophy ["+i+"] = "
                                + "\n name = " + objects.get(i).getName()
                                + objects.get(i).getDescription());

                        trophies.add(0,objects.get(i));
                        trophyAdapter.notifyItemInserted(trophies.size()-1);
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
