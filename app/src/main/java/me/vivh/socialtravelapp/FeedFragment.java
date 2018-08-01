package me.vivh.socialtravelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.socialtravelapp.model.CheckInPost;
import me.vivh.socialtravelapp.model.Post;


public class FeedFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    @BindView(R.id.FeedSwipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvFeed) RecyclerView rvFeed;
    FeedAdapter feedAdapter;
    List<Post> posts;
    List<CheckInPost> checkIns;
    private Unbinder unbinder;

    private FeedAdapter.Callback callback;

    public FeedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        unbinder = ButterKnife.bind(this, view);
        posts = new ArrayList<>();
        feedAdapter = new FeedAdapter(posts, callback);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeed.setAdapter(feedAdapter);
        loadTopPosts();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTopPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ((context instanceof OnFragmentInteractionListener) && (context instanceof FeedAdapter.Callback)) {
            mListener = (OnFragmentInteractionListener) context;
            callback = (FeedAdapter.Callback) context;
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
    }
    public void loadTopPosts() {
        Post.Query postQuery = new Post.Query();
        CheckInPost.Query checkInQuery = new CheckInPost.Query();
        postQuery.include("username");
        postQuery.include("profilePic");

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    posts.clear();
                    posts.addAll(objects);
                    feedAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });

        checkInQuery.findInBackground(new FindCallback<CheckInPost>() {
            @Override
            public void done(List<CheckInPost> objects, ParseException e) {
                checkIns.clear();
                checkIns.addAll(objects);
                feedAdapter.notifyDataSetChanged();
            }
        });
    }
}
