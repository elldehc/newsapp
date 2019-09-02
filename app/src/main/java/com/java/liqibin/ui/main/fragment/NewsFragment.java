package com.java.liqibin.ui.main.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.liqibin.R;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.category.EditCategoryActivity;
import com.java.liqibin.ui.main.MainActivity;
import com.java.liqibin.ui.news.AllNewsFragment;
import com.java.liqibin.ui.news.ColumnFragment;
import com.java.liqibin.ui.adapter.TabAdapter;

import km.lmy.searchview.SearchView;

public class NewsFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private ViewPager viewPager;

    private SearchView searchView;

    public static final int EDIT_CATEGORY_REQUESt_CODE = 1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        toolbar = view.findViewById(R.id.newsToolbar);
        mainActivity.setSupportActionBar(toolbar);

        tabLayout = mainActivity.findViewById(R.id.newsTabLayout);
        viewPager = mainActivity.findViewById(R.id.newsViewPager);
        tabAdapter = new TabAdapter(mainActivity.getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageView edit = view.findViewById(R.id.edit_column);
        edit.setOnClickListener((v) -> {
            Intent intent = new Intent(mainActivity, EditCategoryActivity.class);
            startActivityForResult(intent, EDIT_CATEGORY_REQUESt_CODE);
        });

        searchView = view.findViewById(R.id.searchView);
        mainActivity.setSearchView(searchView);
        mainActivity.setupSearchView(searchView);
        onColumnChanged();
    }

    public SearchView getSearchView() {
        return searchView;
    }

    private void onColumnChanged() {
        SQLiteDatabase database = NewsDatabase.getReadable();
        Cursor cursor = database.query("categories", new String[]{"*"},
                "position >= 0", null, null, null, "position");
        tabAdapter.clear();
        tabAdapter.addFragment(new AllNewsFragment(), "最新");

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String category = cursor.getString(1);
            tabAdapter.addFragment(ColumnFragment.newInstance(category), category);
            cursor.moveToNext();
        }
        cursor.close();
        viewPager.setAdapter(tabAdapter);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CATEGORY_REQUESt_CODE) {
            boolean modified = data.getBooleanExtra("modified", false);
            switch (resultCode) {
                case EditCategoryActivity.FINISH_RESULT_CODE:
                    if (modified) onColumnChanged();
                    break;
                case EditCategoryActivity.JUMP_RESULT_CODE:
                    if (modified) onColumnChanged();
                    int position = data.getIntExtra("target", 0);
                    viewPager.setCurrentItem(position);
                    break;
            }
        }
    }
}
