package com.java.liqibin.ui.task;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.http.NewsDownloader;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;

import java.lang.ref.WeakReference;

public class LoadNewsTask extends AsyncTask<NewsQuery, Void, Boolean> {
    private WeakReference<Activity> activity;
    private WeakReference<RecyclerView> view;
    private NewsRecyclerViewAdapter adapter;
    private QueryHelper helper;

    public LoadNewsTask(WeakReference<Activity> activity, WeakReference<RecyclerView> recyclerView, QueryHelper helper) {
        this.activity = activity;
        this.view = recyclerView;
        this.helper = helper;
    }

    @Override
    protected Boolean doInBackground(NewsQuery... queries) {
        return NewsDownloader.fetch(queries[0]);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (success) {
            if (adapter == null) {
                RecyclerView v = view.get();
                if (v != null) {
                    Cursor cursor = helper.query();
                    adapter = new NewsRecyclerViewAdapter(activity.get(), cursor);
                    v.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
                    v.setAdapter(adapter);
                }
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(activity.get(), "Failed to fetch news", Toast.LENGTH_SHORT).show();
        }
    }

    @FunctionalInterface
    public interface QueryHelper {
        Cursor query();
    }
}
