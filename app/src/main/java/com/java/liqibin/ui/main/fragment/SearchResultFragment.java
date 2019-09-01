package com.java.liqibin.ui.main.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.task.OfflineLoadNewsTask;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class SearchResultFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView newsList;
    private SmartRefreshLayout refreshLayout;

    private OfflineLoadNewsTask.QueryHelper queryHelper;
    private OfflineLoadNewsTask.QueryHelper loadMoreHelper;
    private Cursor cursor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;

        toolbar = view.findViewById(R.id.search_result_toolbar);
        activity.setSupportActionBar(toolbar);

        newsList = view.findViewById(R.id.newsList);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        String word = getArguments().getString("word");

        queryHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    null, null, null, null, null, "15");
            return cursor;
        };

        loadMoreHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    null, null, null, null, null,
                    Integer.toString(cursor.getCount() + 15));
            return cursor;
        };

        search(word);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void search(String s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
}
