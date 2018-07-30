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

import com.parse.Parse;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MemberProfileFragment extends Fragment {

    @BindView(R.id.tvUserName)
    TextView tvUsername;
    @BindView(R.id.tvHomeLoc) TextView tvHomeLoc;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.tvNumPastTrips) TextView tvNumPastTrips;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.tvUpcoming) TextView tvUpcoming;


    private Unbinder unbinder;

    ParseUser user;


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
        return view;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
