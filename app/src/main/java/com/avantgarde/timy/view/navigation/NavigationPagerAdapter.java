package com.avantgarde.timy.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NavigationPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public NavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    public void setFragments(Fragment[] fragments) {
        this.fragments = fragments;
    }

}
