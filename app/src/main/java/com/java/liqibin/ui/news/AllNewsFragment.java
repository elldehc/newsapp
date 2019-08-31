package com.java.liqibin.ui.news;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.task.LoadNewsTask;
import com.java.liqibin.model.bean.DateTime;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.ui.task.RefreshTask;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


public class AllNewsFragment extends Fragment {
    private Cursor cursor = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        RecyclerView newsList = view.findViewById(R.id.newsList);

        LoadNewsTask.QueryHelper queryHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    null, null, null, null, "publishTime desc", "15");
            return cursor;
        };

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener((layout) -> {
            new RefreshTask(activity, newsList, queryHelper, (SmartRefreshLayout) layout)
                    .execute(new NewsQuery().setEndDate(DateTime.now()));
        });

        new LoadNewsTask(activity, newsList, queryHelper)
                .execute(new NewsQuery().setEndDate(DateTime.now()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

}
