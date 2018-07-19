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
import me.vivh.socialtravelapp.model.Trip;

public class MainActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener, FeedFragment.OnFragmentInteractionListener,
TripListFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, TripDetailFragment.OnFragmentInteractionListener, TripAdapter.Callback{

    private final List<Fragment> fragments = new ArrayList<>();
    private BottomNavAdapter adapter;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments.add(new FeedFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new TripListFragment());
        fragments.add(new ProfileFragment());
        fragments.add(new SuggestionFragment());
        fragments.add(new MapsFragment());
        fragments.add(new AttractionFragment());
        fragments.add(new TripDetailFragment());

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
                        viewPager.setCurrentItem(0, false);
                        return true;
                    case R.id.action_explore:
                        viewPager.setCurrentItem(1, false);
                        return true;
                    case R.id.action_trips:
                        viewPager.setCurrentItem(2, false);
                        return true;
                    case R.id.action_profile:

                        viewPager.setCurrentItem(3);
                        viewPager.setCurrentItem(3, false);
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
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void openTripDetail(@NonNull Trip trip) {
        ((TripDetailFragment)fragments.get(7)).trip = trip;
        viewPager.setCurrentItem(7, false);
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
}
