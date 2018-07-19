package me.vivh.socialtravelapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmindlin on 7/17/18.
 */

public class SuggestionFragment extends Fragment {
    private final List<Fragment> fragments = new ArrayList<>();
    MainActivity.BottomNavAdapter adapter;

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_suggestion, parent, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.vpSuggest);
        bottomNavigationView = view.findViewById(R.id.tripNavigation);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_explore, menu);
        fragments.add(new AttractionFragment());
        fragments.add(new MapsFragment());
        adapter = new MainActivity.BottomNavAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.action_list);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.action_map);
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
                    case R.id.action_list:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_map:
                        viewPager.setCurrentItem(1);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


}
