package com.phearom.um.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phearom on 7/15/16.
 */
public class FragmentPager extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public FragmentPager(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    public void add(BaseFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public void set(int i, BaseFragment baseFragment) {
        if (fragments.size() > 0)
            fragments.set(0,baseFragment);
        notifyDataSetChanged();
    }
}
