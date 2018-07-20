package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.Parse;
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

    @BindView(R.id.rvMembers) RecyclerView rvMembers;

    private Trip trip;
    private TripMemberAdapter.CallbackMember callbackMember;
    private ArrayList<ParseUser> members;
    private TripMemberAdapter memberAdapter;
    private Unbinder unbinder;
    Context context;

    public TripMemberFragment(Trip trp) {
        trip = trp;
    }

    public  TripMemberFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_member, container, false);

        unbinder = ButterKnife.bind(this, view);

        members = new ArrayList<>();
        memberAdapter = new TripMemberAdapter(members, callbackMember);

        rvMembers.setLayoutManager(new LinearLayoutManager(context));
        rvMembers.setAdapter(memberAdapter);

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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadMembers(){
//
//        List<ParseUser> users = trip.getList("user");

        try{
            ParseRelation relation = trip.getRelation("user");
            ParseQuery query = relation.getQuery();

            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    Log.d("relation", objects.toString());
                    members.clear();
                    members.addAll(objects);
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

}
