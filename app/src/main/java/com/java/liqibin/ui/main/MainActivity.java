package com.java.liqibin.ui.main;

import android.app.UiModeManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.liqibin.R;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.main.fragment.NewsFragment;
import com.java.liqibin.ui.main.fragment.RecommendationFragment;
import com.java.liqibin.ui.main.fragment.SearchResultFragment;
import com.java.liqibin.ui.main.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

import km.lmy.searchview.SearchView;

public class MainActivity extends AppCompatActivity {

    // Fragment
    private NewsFragment newsFragment;
    private RecommendationFragment recommendationFragment;
    private UserFragment userFragment;
    private SearchResultFragment searchResultFragment;
    private Fragment currentFragment;

    private SearchView searchView;
    private List<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置搜索历史
        history = new ArrayList<>();
        SQLiteDatabase database = NewsDatabase.getReadable();
        try (Cursor cursor = database.query("history", new String[]{"id", "item"},
                null, null, null, null, "id desc")) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                history.add(cursor.getString(1));
            }
        }

        // 设置Fragment
        newsFragment = new NewsFragment();
        recommendationFragment = new RecommendationFragment();
        userFragment = new UserFragment();
        searchResultFragment = new SearchResultFragment();

        currentFragment = newsFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, currentFragment).commit();

        // 设置底部导航栏
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_news:
                    setSupportActionBar(newsFragment.getToolbar());
                    switchFragment(newsFragment);
                    return true;
                case R.id.navigation_recommendation:
                    setSupportActionBar(newsFragment.getToolbar());
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                searchView.autoOpenOrClose();
                return true;
            case R.id.toolbar_night:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ?
                        AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchFragment(Fragment to) {
        if (currentFragment != to) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                fragmentTransaction.hide(currentFragment).add(R.id.container_frame, to).commit();
            } else {
                fragmentTransaction.hide(currentFragment).show(to).commit();
            }
            currentFragment = to;
        }
        if (to == newsFragment) {
            searchView = newsFragment.getSearchView();
            if (searchView != null) {
                searchView.setNewHistoryList(history);
            }
        } else if (to == searchResultFragment) {
            searchView = searchResultFragment.getSearchView();
            if (searchView != null) {
                searchView.setNewHistoryList(history);
            }
        }
    }

    //    @Override
    public void onQueryTextSubmit(String s) {
        searchView.autoOpenOrClose();
        history.remove(s);
        history.add(0, s);
        searchView.setNewHistoryList(history);
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
    }

    public void setupSearchView(SearchView searchView) {
        searchView.setOnSearchActionListener(this::onQueryTextSubmit);
        searchView.setNewHistoryList(history);
        searchView.setHistoryItemClickListener(((historyStr, position) -> {
            searchView.setSearchEditText(historyStr);
        }));
        searchView.setOnCleanHistoryClickListener((() -> {
            SQLiteDatabase database = NewsDatabase.getWritable();
            database.delete("history", null, null);
        }));
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isOpen()) {
            searchView.close();
        } else if (currentFragment == searchResultFragment) {
            setSupportActionBar(newsFragment.getToolbar());
            switchFragment(newsFragment);
        } else {
            super.onBackPressed();
        }
    }

}
