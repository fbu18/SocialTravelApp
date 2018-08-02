package me.vivh.socialtravelapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

    Trip trip;
    Context context;
    private Unbinder unbinder;
    MainActivity.BottomNavAdapter adapter;
    Boolean alreadyCheckedIn = false;
    private ArrayList<ParseUser> membersCheckedIn;


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
        unbinder = ButterKnife.bind(this, view);
        membersCheckedIn = new ArrayList<>();

        try{
            tvTripName.setText(trip.getName());
            tvDate.setText(trip.getDateString());
            tvDescription.setText(trip.getDescription());
            Glide.with(context)
                    .load(trip.getAttraction().fetchIfNeeded().getParseFile("image").getUrl())
                    .into(ivAttractionPic);
        }catch(Exception e){
            e.printStackTrace();
        }

        setUpFragments();
        setUpButtons(view);



        return view;

    }

    public void setUpButtons(final View view){

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on some click or some loading we need to wait for...
                final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pbLoading);
                pb.setVisibility(ProgressBar.VISIBLE);

                ParseRelation checkedInRelation = trip.getRelation("usersCheckedIn");
                ParseQuery checkedInQuery = checkedInRelation.getQuery();

                checkedInQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        Log.d("relation", "Members checked in" + objects.toString());
                        membersCheckedIn.clear();
                        membersCheckedIn.addAll(objects);

                        if (membersCheckedIn.contains(ParseUser.getCurrentUser())){
                            alreadyCheckedIn = true;
                        }

                        if (!alreadyCheckedIn){
                            trip.setCheckIn(ParseUser.getCurrentUser());
                            alreadyCheckedIn = true;
                            btnCheckIn.setText("Check out");
                        }
                        else {
                            trip.removeCheckIn(ParseUser.getCurrentUser());
                            btnCheckIn.setText("Check in");
                            alreadyCheckedIn = false;
                            //Toast.makeText(getActivity(), "Already checked in!", Toast.LENGTH_LONG).show();
                        }
                        // reload TripMemberFragment to show marker
                        setUpFragments();
                        pb.setVisibility(ProgressBar.INVISIBLE);

                    }
                });


            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ParseUser user = ParseUser.getCurrentUser();

                if(btnJoin.getText().equals("Leave Group")){
                    trip.leaveTrip(user, context);
                    btnJoin.setText("Join Group");
                    btnJoin.setBackgroundColor(Color.parseColor("#ff3897f0"));

                }else{
                    trip.joinTrip(user, context);
                    btnJoin.setText("Leave Group");
                    btnJoin.setBackgroundColor(Color.LTGRAY);
                }

            }
        });
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
        setUpFragments();
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
