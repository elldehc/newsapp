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
import com.java.liqibin.model.bean.DateTime;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.task.LoadNewsTask;

import java.lang.ref.WeakReference;

public class ColumnFragment extends Fragment {
    private String columnName;
    private Cursor cursor;

    public ColumnFragment(String columnName) {
        this.columnName = columnName;
    }

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

        new LoadNewsTask(activity, newsList, () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime", "category"},
                    "category=?", new String[]{columnName}, null, null, "publishTime desc");
            return cursor;
        }).execute(new NewsQuery().setCategories(columnName).setEndDate(DateTime.now()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
