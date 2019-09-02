package com.java.liqibin.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liqibin.R;
import com.java.liqibin.ui.main.fragment.NewsFragment;
import com.java.liqibin.ui.main.fragment.RecommendationFragment;
import com.java.liqibin.ui.main.fragment.SearchResultFragment;
import com.java.liqibin.ui.main.fragment.UserFragment;
import com.java.liqibin.ui.news.FavouriteFragment;

public class FavouriteActivity extends AppCompatActivity{

    // Fragment
    private FavouriteFragment favouriteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar=findViewById(R.id.favourite_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // 设置Fragment
        favouriteFragment=new FavouriteFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.favourite_container_frame, favouriteFragment).commit();


    }

}
