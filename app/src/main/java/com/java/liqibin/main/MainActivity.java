package com.java.liqibin.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.java.liqibin.MeFragment;
import com.java.liqibin.NewsFragment;
import com.java.liqibin.R;
import com.java.liqibin.RecommendationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // Fragment
    private NewsFragment newsFragment;
    private RecommendationFragment recommendationFragment;
    private MeFragment meFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置Fragment
        if (savedInstanceState == null) {
            newsFragment = new NewsFragment();
            recommendationFragment = new RecommendationFragment();
            meFragment = new MeFragment();

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
                    switchFragment(currentFragment, meFragment);
                    currentFragment = meFragment;
                    return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment currentFragment, Fragment targetFragment) {
        if (currentFragment != targetFragment) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!targetFragment.isAdded()) {
                fragmentTransaction.hide(currentFragment).add(R.id.container_frame, targetFragment).commit();
            } else {
                fragmentTransaction.hide(currentFragment).show(targetFragment).commit();
            }
        }
    }
}
