package com.java.liqibin.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liqibin.R;
import com.java.liqibin.ui.main.fragment.NewsFragment;
import com.java.liqibin.ui.main.fragment.RecommendationFragment;
import com.java.liqibin.ui.main.fragment.SearchResultFragment;
import com.java.liqibin.ui.main.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Fragment
    private NewsFragment newsFragment;
    private RecommendationFragment recommendationFragment;
    private UserFragment userFragment;
    private SearchResultFragment searchResultFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置Fragment
        if (savedInstanceState == null) {
            newsFragment = new NewsFragment();
            recommendationFragment = new RecommendationFragment();
            userFragment = new UserFragment();
            searchResultFragment = new SearchResultFragment();

            currentFragment = newsFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, currentFragment).commit();
        }

        // 设置底部导航栏
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_news:
                    setSupportActionBar(newsFragment.getToolbar());
                    switchFragment(newsFragment);
                    return true;
                case R.id.navigation_recommendation:
                    switchFragment(recommendationFragment);
                    return true;
                case R.id.navigation_me:
                    switchFragment(userFragment);
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    private void switchFragment(Fragment to) {
        if (currentFragment != to) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                fragmentTransaction.hide(currentFragment).add(R.id.container_frame, to).commit();
            } else {
                fragmentTransaction.hide(currentFragment).show(to).commit();
            }
            currentFragment = to;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (searchResultFragment.getView() == null) {
            Bundle bundle = new Bundle();
            bundle.putString("word", s);
            searchResultFragment.setArguments(bundle);
            switchFragment(searchResultFragment);

        } else {
            searchResultFragment.search(s);
            setSupportActionBar(searchResultFragment.getToolbar());
            switchFragment(searchResultFragment);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == searchResultFragment) {
            setSupportActionBar(newsFragment.getToolbar());
            switchFragment(newsFragment);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
