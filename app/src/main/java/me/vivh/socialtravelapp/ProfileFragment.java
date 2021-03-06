package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Trip;


public class ProfileFragment extends Fragment {

    @BindView(R.id.btnLogOut) Button btnLogOut;
    @BindView(R.id.tvDisplayName) TextView tvDisplayName;
    @BindView(R.id.tvHomeLoc) TextView tvHomeLoc;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.ivProfilePic) ImageView ivProfilePic;
    @BindView(R.id.btnEditProfile) TextView editProfileBtn;
    @BindView(R.id.tvBio) TextView tvBio;

    private ArrayList<Trip> pastTripArray;

    Calendar cal = new GregorianCalendar();
    Date today;

    ParseUser currentUser;

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

        // don't display back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        today = cal.getTime();


        btnLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.logout();
            }
        });

        pastTripArray = new ArrayList<>();

        currentUser = ParseUser.getCurrentUser();

        try {
            tvDisplayName.setText(currentUser.getString("displayName"));
            tvHomeLoc.setText(currentUser.getString("home"));
            tvPoints.setText(currentUser.getNumber("points").toString());
            tvBio.setText(currentUser.getString("bio"));

            String profilePicUrl = currentUser.getParseFile("profilePic").getUrl();
            Glide.with(getContext()).load(profilePicUrl)
                    .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_perm_identity_black_24dp)
                                    .circleCrop())
                    .into(ivProfilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openEditProfile();
            }
        });

        ivProfilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.openEditProfile();
            }
        });

        TrophyListFragment trophyListFrag = TrophyListFragment.newInstance(currentUser);
        setTrophyListFragment(trophyListFrag);

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



    @Override
    public void onResume() {
        super.onResume();
        tvPoints.setText(currentUser.getNumber("points").toString());
    }

    /**e1.printStackTrace();
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
        void openEditProfile();
    }

    public void setTrophyListFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flTrophyList, fragment, "ProfileFragment");
        transaction.addToBackStack("ProfileFragment");
        transaction.commitAllowingStateLoss();
    }
}
