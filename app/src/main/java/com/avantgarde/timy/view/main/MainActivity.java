package com.avantgarde.timy.view.main;

import android.support.v4.app.Fragment;

import com.avantgarde.timy.R;
import com.avantgarde.timy.view.navigation.NavigationPagerAdapter;
import com.avantgarde.timy.view.main.fragment.AccountFragment;
import com.avantgarde.timy.view.main.fragment.AssistFragment;
import com.avantgarde.timy.view.main.dashboard.DashboardFragment;
import com.avantgarde.timy.view.main.fragment.GoalsFragment;
import com.avantgarde.timy.view.main.fragment.SocialFragment;
import com.avantgarde.timy.view.navigation.NavigationActivity;
import com.avantgarde.timy.view.navigation.NavigationViewPager;

public class MainActivity extends NavigationActivity {

    private NavigationViewPager fragmentViewPager;

    @Override
    protected void inflate() {
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.container), true);
        fragmentViewPager = findViewById(R.id.viewpager);
        NavigationPagerAdapter navPagerAdapter = new NavigationPagerAdapter(getSupportFragmentManager());
        navPagerAdapter.setFragments(new Fragment[]{
                new AssistFragment(),
                new SocialFragment(),
                new DashboardFragment(),
                new GoalsFragment(),
                new AccountFragment()
        });
        fragmentViewPager.setAdapter(navPagerAdapter);
        fragmentViewPager.setOffscreenPageLimit(4);

        openPage(getIntent().getIntExtra("page", START_PAGE));
    }

    public void openPage(int position) {
        fragmentViewPager.setCurrentItem(position,false);
    }
}