package com.java.liqibin.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.java.liqibin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liqibin.ui.main.fragment.NewsFragment;
import com.java.liqibin.ui.main.fragment.RecommendationFragment;
import com.java.liqibin.ui.main.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    // Fragment
    private NewsFragment newsFragment;
    private RecommendationFragment recommendationFragment;
    private UserFragment userFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置Fragment
        if (savedInstanceState == null) {
            newsFragment = new NewsFragment();
            recommendationFragment = new RecommendationFragment();
            userFragment = new UserFragment();

            currentFragment = newsFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, currentFragment).commit();
        }

        // 设置底部导航栏
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_news:
                    switchFragment(currentFragment, newsFragment);
                    currentFragment = newsFragment;
                    return true;
                case R.id.navigation_recommendation:
                    switchFragment(currentFragment, recommendationFragment);
                    currentFragment = recommendationFragment;
                    return true;
                case R.id.navigation_me:
                    switchFragment(currentFragment, userFragment);
                    currentFragment = userFragment;
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        return true;
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                fragmentTransaction.hide(from).add(R.id.container_frame, to).commit();
            } else {
                fragmentTransaction.hide(from).show(to).commit();
            }
        }
    }
}
