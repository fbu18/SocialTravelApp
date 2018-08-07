package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trip;


public class TripMemberFragment extends Fragment {

    private static final String TRIP_ARG = "trip";

    @BindView(R.id.rvMembers) RecyclerView rvMembers;

    private Trip trip;
    private TripMemberAdapter.CallbackMember callbackMember;
    private ArrayList<ParseUser> members;
    private ArrayList<ParseUser> membersCheckedIn;
    private TripMemberAdapter memberAdapter;
    private Unbinder unbinder;
    Context context;


    public static TripMemberFragment newInstance(Trip trip) {
        TripMemberFragment fragment = new TripMemberFragment();
        Bundle extras = new Bundle();
        extras.putParcelable(TRIP_ARG, trip);
        fragment.setArguments(extras);
        return fragment;
    }

    public TripMemberFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        if (getArguments() != null) {
            trip = getArguments().getParcelable(TRIP_ARG);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_member, container, false);

        unbinder = ButterKnife.bind(this, view);

        members = new ArrayList<>();
        membersCheckedIn = new ArrayList<>();
        memberAdapter = new TripMemberAdapter(members, membersCheckedIn, callbackMember);

        rvMembers.setLayoutManager(new LinearLayoutManager(context));
        rvMembers.setAdapter(memberAdapter);

        loadMembersCheckedIn();
        loadMembers();


        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripMemberAdapter.CallbackMember) {
            callbackMember = (TripMemberAdapter.CallbackMember) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadMembers(){
        try{
            ParseRelation relation = trip.getRelation("user");
            ParseQuery query = relation.getQuery();

            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    Log.d("relation", "Members" + objects.toString());
                    members.clear();
                    members.addAll(objects);
                    memberAdapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadMembersCheckedIn(){
        try{
            ParseRelation checkedInRelation = trip.getRelation("usersCheckedIn");
            ParseQuery checkedInQuery = checkedInRelation.getQuery();

            checkedInQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    Log.d("relation", "Members checked in" + objects.toString());
                    membersCheckedIn.clear();
                    membersCheckedIn.addAll(objects);
                    memberAdapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMembersCheckedIn();
    }
}
