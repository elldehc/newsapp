package com.java.liqibin.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.liqibin.R;
import com.java.liqibin.ui.news.AllNewsFragment;
import com.java.liqibin.ui.news.ColumnFragment;
import com.java.liqibin.ui.adapter.TabAdapter;

public class NewsFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String[] columns = {"娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        assert appCompatActivity != null;
        Toolbar toolbar = appCompatActivity.findViewById(R.id.toolbar);
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("News");
        }

        tabLayout = appCompatActivity.findViewById(R.id.newsTabLayout);
        viewPager = appCompatActivity.findViewById(R.id.newsViewPager);
        TabAdapter tabAdapter = new TabAdapter(appCompatActivity.getSupportFragmentManager());

        tabAdapter.addFragment(new AllNewsFragment(), "News");
        for (String column : columns) {
            tabAdapter.addFragment(new ColumnFragment(column), column);
        }

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
