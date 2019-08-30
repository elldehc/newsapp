package com.java.liqibin.ui.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> tabs;
    private List<String> titles;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        tabs = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        tabs.add(fragment);
        titles.add(title);
    }

    public boolean removeFragment(int position) {
        if (position < tabs.size()) {
            tabs.remove(position);
            titles.remove(position);
            return true;
        }
        return false;
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
