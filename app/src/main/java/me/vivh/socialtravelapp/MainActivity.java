package me.vivh.socialtravelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Trip;

public class MainActivity extends AppCompatActivity implements SuggestionFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        AttractionDetailsFragment.OnFragmentInteractionListener, AttractionListFragment.OnFragmentInteractionListener,
        AttractionAdapter.Callback, FeedFragment.OnFragmentInteractionListener,
        TripListFragment.OnFragmentInteractionListener, TripAdapter.Callback, TripMemberAdapter.CallbackMember,
        MapsFragment.OnFragmentInteractionListener,
        ChatListAdapter.Callback, ChatListFragment.OnFragmentInteractionListener,
        FeedAdapter.Callback, UserAdapter.Callback, EditProfileFragment.OnFragmentInteractionListener,
        MemberProfileFragment.OnFragmentInteractionListener, CustomWindowAdapter.Callback,
        TripBrowseAdapter.Callback{

    public static final int FEED_INDEX = 0;
    public static final int SUGGESTIONS_INDEX = 1;
    public static final int TRIP_LIST_INDEX = 2;
    public static final int CHAT_LIST_INDEX = 3;
    public static final int PROFILE_INDEX = 4;
    public static final int MAPS_INDEX = 5;
    public static final int ATTRACTION_INDEX = 6;
    public static final int TRIP_DETAIL_INDEX = 7;
    public static final int ATTRACTION_DETAILS_INDEX = 8;
    public static final int TRIP_BROWSE_INDEX = 9;
    public static final int EDIT_PROFILE_INDEX = 10;
    public static final int MEMBER_PROFILE_INDEX = 11;

    private final List<Fragment> fragments = new ArrayList<>();
    private Stack<Integer> backStack = new Stack<>();
    private int itemPosition = 0;
    private BottomNavAdapter adapter;
    Map<String,Attraction> dictionary = new HashMap<String, Attraction>();

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // don't display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ButterKnife.bind(this);
        fragments.add(new FeedFragment()); // index 0
        fragments.add(new SuggestionFragment()); // index 1
        fragments.add(new TripListFragment()); // index 2
        fragments.add(new ChatListFragment()); // index 3
        fragments.add(new ProfileFragment()); // index 4
        fragments.add(new MapsFragment()); // index 5
        fragments.add(new AttractionListFragment()); // index 6
        fragments.add(new TripDetailFragment()); // index 7
        fragments.add(new AttractionDetailsFragment()); // index 8
        fragments.add(new TripBrowseFragment()); // index 9
        fragments.add(new EditProfileFragment()); // index 10
        fragments.add(new MemberProfileFragment()); //index 11

        adapter = new BottomNavAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.action_feed);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.action_explore);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.action_trips);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.action_chat);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.action_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_feed:
                        viewPager.setCurrentItem(FEED_INDEX, false);
                        modifyStack(FEED_INDEX);
                        return true;
                    case R.id.action_explore:
                        viewPager.setCurrentItem(SUGGESTIONS_INDEX, false);
                        modifyStack(SUGGESTIONS_INDEX);
                        return true;
                    case R.id.action_trips:
                        viewPager.setCurrentItem(TRIP_LIST_INDEX, false);
                        modifyStack(TRIP_LIST_INDEX);
                        return true;
                    case R.id.action_chat:
                        viewPager.setCurrentItem(CHAT_LIST_INDEX, false);
                        modifyStack(CHAT_LIST_INDEX);
                        return true;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(PROFILE_INDEX, false);
                        modifyStack(PROFILE_INDEX);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    // for chat icon in top corner
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // handle chat button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.miChat) {
            viewPager.setCurrentItem(CHAT_LIST_INDEX, false);
        }
        return super.onOptionsItemSelected(item);
    } */

    static class BottomNavAdapter extends FragmentStatePagerAdapter {

        /**
         * The list of fragments which we are going to be displaying in the view pager.
         */
        private final List<Fragment> fragments;

        public BottomNavAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);

            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void openTripDetail(@NonNull Trip trip) {
        ((TripDetailFragment)fragments.get(TRIP_DETAIL_INDEX)).setTrip(trip);
        viewPager.setCurrentItem(TRIP_DETAIL_INDEX, false);
        modifyStack(TRIP_DETAIL_INDEX);
    }

    @Override
    public void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Logged out successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openAttractionDetails(@NonNull Attraction attraction) {
        ((AttractionDetailsFragment)fragments.get(ATTRACTION_DETAILS_INDEX)).attraction = attraction;
        viewPager.setCurrentItem(ATTRACTION_DETAILS_INDEX, false);
        modifyStack(ATTRACTION_DETAILS_INDEX);
    }

    @Override
    public void openTripBrowse(@NonNull Attraction attraction) {
        ((TripBrowseFragment) fragments.get(TRIP_BROWSE_INDEX)).attraction = attraction;
        viewPager.setCurrentItem(TRIP_BROWSE_INDEX, false);
        ((TripBrowseFragment) fragments.get(TRIP_BROWSE_INDEX)).setAttraction(attraction);
        ((TripBrowseFragment) fragments.get(TRIP_BROWSE_INDEX)).loadTopTrips();
        viewPager.setCurrentItem(TRIP_BROWSE_INDEX);
        modifyStack(TRIP_BROWSE_INDEX);
    }

    public static int getFEED_INDEX() {
        return FEED_INDEX;
    }

    @Override
    public void openChat(@NonNull Trip trip) {
        final Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("trip", trip);
        intent.putExtra("tripId",trip.getObjectId());
        startActivity(intent);
    }
    @Override
    public void openSuggestion() {
        viewPager.setCurrentItem(SUGGESTIONS_INDEX);
        modifyStack(SUGGESTIONS_INDEX);
    }

    @Override
    public void dialPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void openMemberProfile(ParseUser user) {
        String userId = user.getObjectId();
        String currentId = ParseUser.getCurrentUser().getObjectId();
        if(userId.equals(currentId)){
            viewPager.setCurrentItem(PROFILE_INDEX, false);
            modifyStack(PROFILE_INDEX);
        }else{
            ((MemberProfileFragment)fragments.get(MEMBER_PROFILE_INDEX)).setUser(user);

            viewPager.setCurrentItem(MEMBER_PROFILE_INDEX, false);
            ((MemberProfileFragment)fragments.get(MEMBER_PROFILE_INDEX)).updateMemberInfo();
            modifyStack(MEMBER_PROFILE_INDEX);
        }
    }

    @Override
    public void openEditProfile() {
        viewPager.setCurrentItem(EDIT_PROFILE_INDEX, false);
        modifyStack(EDIT_PROFILE_INDEX);
    }

    // build stack for back button flow
    public void modifyStack(int index) {
        if (backStack.empty())
            backStack.push(index);
        else if (backStack.contains(index)) {
            backStack.remove(backStack.indexOf(index));
            backStack.push(index);
        } else {
            backStack.push(index);
        }
    }

    @Override
    public void onBackPressed() {
        if (backStack.size() > 1) {
            backStack.pop();
            viewPager.setCurrentItem(backStack.lastElement(),false);
        } else {
            viewPager.setCurrentItem(FEED_INDEX,false);
        }
    }

    public void clearDictionary() {
        dictionary.clear();
    }
    public void putDictionary(String key, Attraction value) {
        dictionary.put(key, value);
    }
    public Attraction getDictionary(String key) {
        return dictionary.get(key);
    }

}
