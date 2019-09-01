package com.java.liqibin.ui.main.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.java.liqibin.R;
import com.java.liqibin.ui.task.Recommender;

public class RecommendationFragment extends Fragment {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private String[] keywords;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        new Recommender(view,activity).execute();
        toolbar = view.findViewById(R.id.newsToolbar);
        activity.setSupportActionBar(toolbar);
    }
    public Toolbar getToolbar() {
        return toolbar;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }
}
