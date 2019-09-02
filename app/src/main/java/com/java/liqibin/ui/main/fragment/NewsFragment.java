package com.java.liqibin.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Toolbar toolbar;

    private static final String[] columns = {"娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        assert appCompatActivity != null;

        toolbar = view.findViewById(R.id.newsToolbar);
        appCompatActivity.setSupportActionBar(toolbar);


        tabLayout = appCompatActivity.findViewById(R.id.newsTabLayout);
        viewPager = appCompatActivity.findViewById(R.id.newsViewPager);
        TabAdapter tabAdapter = new TabAdapter(appCompatActivity.getSupportFragmentManager());

        tabAdapter.addFragment(new AllNewsFragment(), "最新");
        for (String column : columns) {
            tabAdapter.addFragment(new ColumnFragment(column), column);
        }

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setOnClickListener((v) -> {
                        Toast.makeText(appCompatActivity, "Click", Toast.LENGTH_SHORT).show();
                    });
                    tabView.setOnLongClickListener((v) -> {
                        Toast.makeText(appCompatActivity, "Long Press", Toast.LENGTH_SHORT).show();
                        return true;
                    });
                }
            }
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
