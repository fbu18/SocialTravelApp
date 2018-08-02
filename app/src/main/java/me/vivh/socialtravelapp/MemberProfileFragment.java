package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trip;

public class MemberProfileFragment extends Fragment {

    @BindView(R.id.tvDisplayName) TextView tvDisplayName;
    @BindView(R.id.tvHomeLoc) TextView tvHomeLoc;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.tvNumPastTrips) TextView tvNumPastTrips;
    @BindView(R.id.ivProfilePic) ImageView ivProfilePic;
    @BindView(R.id.tvUpcoming) TextView tvUpcoming;
    @BindView(R.id.tvBio) TextView tvBio;


    private Unbinder unbinder;

    ParseUser user;

    Calendar cal = new GregorianCalendar();
    Date today;

    private ArrayList<Trip> pastTripArray;


    public MemberProfileFragment() {
        // Required empty public constructor
    }

//    public static MemberProfileFragment newInstance(String param1, String param2) {
//        MemberProfileFragment fragment = new MemberProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        updateMemberInfo();

        return view;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void findPastTrips(){

        final Trip.Query tripQuery = new Trip.Query();
        tripQuery.whereLessThan("date", today);
        tripQuery.whereEqualTo("user", user);

        tripQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if(e == null){
                    try{
                        tvNumPastTrips.setText(String.format("%s", objects.size()));
                        pastTripArray.clear();
                        pastTripArray.addAll(objects);
                    }catch(Exception d){
                        d.printStackTrace();
                    }

                }else{
                    Log.d("ProfileFragment", String.format("%s", objects.size()));
                }
            }
        });
    }

    public void findUpcomingTrips(){


        final Trip.Query tripQuery = new Trip.Query();
        tripQuery.whereGreaterThan("date", today);
        tripQuery.whereEqualTo("user", user);

        tripQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if(e == null){
                    tvUpcoming.setText(String.format("%s", objects.size()));
                }else{
                    Log.d("ProfileFragment", String.format("%s", objects.size()));
                }
            }
        });
    }

    public void setUser(ParseUser user){
        this.user = user;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMemberInfo();
    }

    public void updateMemberInfo(){
        today = cal.getTime();

        try {
            tvPoints.setText(user.get("points").toString());
            tvDisplayName.setText(user.getString("displayName"));
            tvHomeLoc.setText(user.getString("home"));
            tvBio.setText(user.getString("bio"));

            String profilePicUrl = user.getParseFile("profilePic").getUrl();
            Glide.with(getContext()).load(profilePicUrl)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_perm_identity_black_24dp)
                                    .circleCrop())
                    .into(ivProfilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findUpcomingTrips();
        findPastTrips();
    }

}
