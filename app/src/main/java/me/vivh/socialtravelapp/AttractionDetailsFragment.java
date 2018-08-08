package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Review;


public class AttractionDetailsFragment extends Fragment {
    @BindView(R.id.tvAttrName) TextView tvAttrName;
    @BindView(R.id.ivAttrPic) ImageView ivAttrPic;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.tvAttrDesc) TextView tvAttrDesc;
    @BindView(R.id.tvAttrAddress) TextView tvAttrAddress;
    @BindView(R.id.tvAttrPhoneNumber) TextView tvAttrPhoneNumber;
    @BindView(R.id.tvAttrWebsite) TextView tvAttrWebsite;
    @BindView(R.id.btnChoose) Button btnTrip;
    @BindView(R.id.ivAttrPhoneNumber) ImageView ivAttrPhoneNumber;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    private final List<Attraction> attractions = new ArrayList<>();
    Attraction attraction;
    Context context;
    private Unbinder unbinder;
    private AttractionDetailsFragment.OnFragmentInteractionListener listener;
    private ReviewAdapter adapter;
    private List<Review> reviews;

    public AttractionDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_attraction_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        reviews = new ArrayList<>();
        adapter = new ReviewAdapter(reviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(context));
        rvReviews.setAdapter(adapter);

        loadTopReviews();

        try{
            tvAttrName.setText(attraction.getName());
            tvAttrDesc.setText(attraction.getDescription());
            tvAttrAddress.setText(attraction.getAddress());
            rbVoteAverage.setNumStars(5);
            rbVoteAverage.setRating(attraction.getRating().floatValue());
            tvAttrPhoneNumber.setText(attraction.getPhoneNumber());
            tvAttrWebsite.setText(attraction.getWebsite());
            tvPoints.setText(String.valueOf(attraction.getPoints()));
            Glide.with(context).load(attraction.getImage().getUrl())
                    .apply(
                            RequestOptions.placeholderOf(R.color.placeholderColor)
                                    .centerCrop())
                    .into(ivAttrPic);
        }catch(Exception e){
            e.printStackTrace();
        }

        btnTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = attraction.getName();
                listener.openTripBrowse(attraction);
            }
        });

        tvAttrPhoneNumber.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                listener.dialPhone(attraction.getPhoneNumber());
            }
        });

        ivAttrPhoneNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.dialPhone(attraction.getPhoneNumber());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AttractionDetailsFragment.OnFragmentInteractionListener) {
            listener = (AttractionDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void loadTopReviews(){
        ParseQuery<Review> parseQuery = ParseQuery.getQuery(Review.class);
        parseQuery.whereEqualTo("attraction", attraction);
        parseQuery.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> objects, ParseException e) {
                reviews.clear();
                reviews.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void openTripBrowse(Attraction attraction);

        void dialPhone(String phoneNumber);
    }
}
