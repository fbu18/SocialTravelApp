package me.vivh.socialtravelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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


public class ProfileFragment extends Fragment {

    @BindView(R.id.btnLogOut) Button btnLogOut;
    @BindView(R.id.tvUserName)
    TextView tvUsername;
    @BindView(R.id.tvHomeLoc) TextView tvHomeLoc;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.tvNumPastTrips) TextView tvNumPastTrips;
    @BindView(R.id.ivProfilePic) ImageView ivProfilePic;

    private ArrayList<Trip> pastTrips;

    Calendar cal = new GregorianCalendar();

    ParseUser currentUser;
    int numTrips;

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.logout();
            }
        });

        numTrips = 0;
        pastTrips = new ArrayList<>();

        currentUser = ParseUser.getCurrentUser();

        tvUsername.setText(currentUser.getUsername());
        tvHomeLoc.setText(currentUser.getString("home"));
        tvPoints.setText(currentUser.getNumber("points").toString());

        Glide.with(getContext()).load(ParseUser.getCurrentUser().getParseFile("profilePic").getUrl())
                .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_perm_identity_black_24dp)
                                .circleCrop())
                .into(ivProfilePic);

        findPastTrips();
        tvNumPastTrips.setText(String.format("%s", numTrips));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void findPastTrips(){

        Date today = cal.getTime();

        ParseQuery<Trip> tripQuery = ParseQuery.getQuery("date");
        tripQuery.whereLessThan("date", today);

        tripQuery.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                numTrips = objects.size();
                pastTrips.clear();
                pastTrips.addAll(objects);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void logout();
    }
}
