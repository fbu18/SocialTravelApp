package me.vivh.socialtravelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.socialtravelapp.model.Attraction;
import me.vivh.socialtravelapp.model.Trip;

public class MainActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        AttractionDetailsFragment.OnFragmentInteractionListener, AttractionFragment.OnFragmentInteractionListener,
        AttractionAdapter.Callback, FeedFragment.OnFragmentInteractionListener,
        TripListFragment.OnFragmentInteractionListener, TripAdapter.Callback, TripMemberAdapter.CallbackMember,
        MapsFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener, ChatListAdapter.Callback, ChatListFragment.OnFragmentInteractionListener{

    public static final int FEED_INDEX = 0;
    public static final int EXPLORE_INDEX = 1;
    public static final int TRIP_LIST_INDEX = 2;
    public static final int PROFILE_INDEX = 3;
    public static final int SUGGESTIONS_INDEX = 4;
    public static final int MAPS_INDEX = 5;
    public static final int ATTRACTION_INDEX = 6;
    public static final int TRIP_DETAIL_INDEX = 7;
    public static final int ATTRACTION_DETAILS_INDEX = 8;
    public static final int TRIP_BROWSE_INDEX = 9;
    public static final int EDIT_PROFILE_INDEX = 10;
    public static final int CHAT_LIST_INDEX = 11;
    public static final int CHAT_INDEX = 12;


    private final List<Fragment> fragments = new ArrayList<>();
    private BottomNavAdapter adapter;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments.add(new FeedFragment()); // index 0
        fragments.add(new ExploreFragment()); // index 1
        fragments.add(new TripListFragment()); // index 2
        fragments.add(new ProfileFragment()); // index 3
        fragments.add(new SuggestionFragment()); // index 4
        fragments.add(new MapsFragment()); // index 5
        fragments.add(new AttractionFragment()); // index 6
        fragments.add(new TripDetailFragment()); // index 7
        fragments.add(new AttractionDetailsFragment()); // index 8
        fragments.add(new TripBrowseFragment()); // index 9
        fragments.add(new EditProfileFragment()); // index 10
        fragments.add(new ChatListFragment()); // index 11
        fragments.add(new ChatFragment()); // index 12

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
                        return true;
                    case R.id.action_explore:
                        viewPager.setCurrentItem(EXPLORE_INDEX, false);
                        return true;
                    case R.id.action_trips:
                        viewPager.setCurrentItem(TRIP_LIST_INDEX, false);
                        return true;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(PROFILE_INDEX, false);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
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
    }

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
    }

    @Override
    public void openMemberDetail(@NonNull ParseUser user) {

    }
    @Override
    public void openTripBrowse(@NonNull Attraction attraction) {
        ((TripBrowseFragment) fragments.get(TRIP_BROWSE_INDEX)).setAttraction(attraction);
        viewPager.setCurrentItem(TRIP_BROWSE_INDEX);
    }

    public static int getFEED_INDEX() {
        return FEED_INDEX;
    }

    @Override
    public void openChat(@NonNull Trip trip) {
        ((ChatFragment)fragments.get(CHAT_INDEX)).trip = trip;
        viewPager.setCurrentItem(CHAT_INDEX, false);
    }


    public static int getEXPLORE_INDEX() {
        return EXPLORE_INDEX;
    }

    public static int getTRIP_LIST_INDEX() {
        return TRIP_LIST_INDEX;
    }

    public static int getPROFILE_INDEX() {
        return PROFILE_INDEX;
    }

    public static int getSUGGESTIONS_INDEX() {
        return SUGGESTIONS_INDEX;
    }

    public static int getMAPS_INDEX() {
        return MAPS_INDEX;
    }

    public static int getATTRACTION_INDEX() {
        return ATTRACTION_INDEX;
    }

    public static int getTRIP_DETAIL_INDEX() {
        return TRIP_DETAIL_INDEX;
    }

    public static int getATTRACTION_DETAILS_INDEX() {
        return ATTRACTION_DETAILS_INDEX;
    }

    public static int getTRIP_BROWSE_INDEX() {
        return TRIP_BROWSE_INDEX;
    }

    public static int getEDIT_PROFILE_INDEX() {
        return EDIT_PROFILE_INDEX;
    }

    public static int getCHAT_LIST_INDEX() {
        return CHAT_LIST_INDEX;
    }

    public static int getCHAT_INDEX() {
        return CHAT_INDEX;
    }
}
