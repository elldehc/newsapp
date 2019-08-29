package com.java.liqibin.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.java.liqibin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    /* 以下是 private 部分 */

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
