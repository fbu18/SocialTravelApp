package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class TripDetailFragment extends Fragment {

    @BindView(R.id.ivAttractionPic) ImageView ivAttractionPic;
    @BindView(R.id.tvTripName) TextView tvTripName;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.vpTrip) ViewPager vpTrip;
    @BindView(R.id.tripNavigation) BottomNavigationView tripNavigation;
    @BindView(R.id.btnCheckIn) Button btnCheckIn;
    @BindView(R.id.btnJoin) Button btnJoin;
    @BindView(R.id.pbLoading) ProgressBar pb;

    Trip trip;
    Context context;
    private Unbinder unbinder;
    MainActivity.BottomNavAdapter adapter;
    Boolean alreadyCheckedIn = false;
    private ArrayList<ParseUser> membersCheckedIn;
    Boolean inGroup = false;


    private final List<Fragment> fragments = new ArrayList<>();

    public TripDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        unbinder = ButterKnife.bind(this, view);
        membersCheckedIn = new ArrayList<>();

        try{
            tvTripName.setText(trip.getName());
            tvDate.setText(trip.getDateWithYearString());
            tvDescription.setText(trip.getDescription());
            Glide.with(context)
                    .load(trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl())
                    .into(ivAttractionPic);
        }catch(Exception e){
            e.printStackTrace();
        }

        ParseUser user = ParseUser.getCurrentUser();
        queryCheckedIn(user);
        queryJoined(user);

        setUpButtons(user);

        return view;

    }

    public void setUpButtons(final ParseUser user){

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on some click or some loading we need to wait for...
                pb.setVisibility(ProgressBar.VISIBLE);

                if(alreadyCheckedIn){
                    trip.removeCheckIn(user);
                    btnCheckIn.setText("Check in");
                    alreadyCheckedIn = false;
                    btnCheckIn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }else{

                    if(inGroup){
                        trip.setCheckIn(user);
                        alreadyCheckedIn = true;
                        btnCheckIn.setText("Check out");
                        btnCheckIn.setBackgroundColor(getResources().getColor(R.color.pressed_button_gray));
                    }else{
                        Toast.makeText(context, "Must join group to check in", Toast.LENGTH_SHORT).show();
                    }

                }

                setUpFragments();
                pb.setVisibility(ProgressBar.INVISIBLE);
                setUpFragments(); // TODO - remove because this is bad practice!
            }
        });



        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // on some click or some loading we need to wait for...
                pb.setVisibility(ProgressBar.VISIBLE);

                if(inGroup){
                    inGroup = false;
                    alreadyCheckedIn = false;
                    trip.leaveTrip(user, context);
                    trip.removeCheckIn(user);
                    btnJoin.setText("Join Group");
                    btnJoin.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    btnCheckIn.setText("Check in");
                    btnCheckIn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }else{
                    inGroup = true;
                    trip.joinTrip(user, context);
                    btnJoin.setText("Leave Group");
                    btnJoin.setBackgroundColor(getResources().getColor(R.color.pressed_button_gray));
                }

                setUpFragments();
                pb.setVisibility(ProgressBar.INVISIBLE);
                setUpFragments(); // TODO - remove because this is bad practice!
            }
        });
    }

    public void queryCheckedIn(final ParseUser user){


        try {
            ParseRelation checkedInRelation = trip.getRelation("usersCheckedIn");
            ParseQuery checkedInQuery = checkedInRelation.getQuery();

            checkedInQuery.findInBackground(new FindCallback<ParseUser>(){
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    membersCheckedIn.clear();
                    membersCheckedIn.addAll(objects);

                    for(ParseUser usr: objects){
                        if((usr.getUsername()).equals(user.getUsername())){
                            alreadyCheckedIn = true;
                            btnCheckIn.setBackgroundColor(getResources().getColor(R.color.pressed_button_gray));
                            btnCheckIn.setText("Check out");
                        }
                    }

                    setUpFragments();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryJoined(final ParseUser user){
        try {
            ParseRelation inGroupRelation = trip.getRelation("user");
            ParseQuery inGroupQuery = inGroupRelation.getQuery();

            inGroupQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    for(ParseUser usr: objects){
                        if((usr.getUsername()).equals(user.getUsername())){
                            inGroup = true;
                            btnJoin.setBackgroundColor(getResources().getColor(R.color.pressed_button_gray));
                            btnJoin.setText("Leave Group");
                        }
                    }

                    setUpFragments();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onResume() {
        super.onResume();
        queryCheckedIn(ParseUser.getCurrentUser());
        queryJoined(ParseUser.getCurrentUser());
//        setUpFragments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpFragments(){

        TripMemberFragment tripMemberFrag = TripMemberFragment.newInstance(trip);
        TripPhotosFragment tripPhotosFrag = TripPhotosFragment.newInstance(trip);

        fragments.clear();
        fragments.add(tripMemberFrag);
        fragments.add(tripPhotosFrag);
        adapter = new MainActivity.BottomNavAdapter(getChildFragmentManager(), fragments);
        vpTrip.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vpTrip.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        tripNavigation.setSelectedItemId(R.id.action_members);
                        break;
                    case 1:
                        tripNavigation.setSelectedItemId(R.id.action_photos);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });
        tripNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_members:
                        ((TripMemberFragment)fragments.get(0)).setTrip(trip);
                        vpTrip.setCurrentItem(0);
                        return true;
                    case R.id.action_photos:
                        vpTrip.setCurrentItem(1);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
