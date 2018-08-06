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
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trophy;

public class TrophyListFragment extends Fragment {

    private static final String KEY_USER = "user";
    ParseUser user;
    ArrayList<Trophy> trophies;
    RecyclerView rvTrophies;
    TrophyAdapter trophyAdapter;
    AsyncHttpClient client;
    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;
    private ProgressBar pb;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrophyListFragment() {
    }

    public static TrophyListFragment newInstance(ParseUser user) {
        TrophyListFragment fragment = new TrophyListFragment();
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_USER, user);
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            user = getArguments().getParcelable(KEY_USER);
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_trophy_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        client = new AsyncHttpClient();
        trophies = new ArrayList<>();

        trophyAdapter = new TrophyAdapter(trophies);
        rvTrophies = (RecyclerView) rootView.findViewById(R.id.rvTrophies);
        pb = (ProgressBar) rootView.findViewById(R.id.pbLoading);

        loadTrophies();

        int numColumns = 3;
        rvTrophies.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvTrophies.setAdapter(trophyAdapter);

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
        pb.setVisibility(ProgressBar.VISIBLE);

        try{
            ParseRelation trophyRelation = user.getRelation("trophies");
            ParseQuery trophyQuery = trophyRelation.getQuery();
            //final Trophy.Query trophyQuery = new Trophy.Query();
            //trophyQuery.getTop().withName();
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
                            pb.setVisibility(ProgressBar.INVISIBLE);
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public interface OnFragmentInteractionListener {

    }
}
