package com.java.liqibin.ui.task;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.ui.adapter.NewsOfflineRecyclerViewAdapter;

import java.lang.ref.WeakReference;

public class OfflineLoadNewsTask extends AsyncTask<Void, Void, Cursor> {
    private WeakReference<Activity> refActivity;
    private WeakReference<RecyclerView> refView;
    private QueryHelper helper;

    public OfflineLoadNewsTask(Activity activity, RecyclerView recyclerView, QueryHelper helper) {
        this.refActivity = new WeakReference<>(activity);
        this.refView = new WeakReference<>(recyclerView);
        this.helper = helper;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        return helper.query();
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        RecyclerView view = refView.get();
        if (view != null) {
            RecyclerView.Adapter adapter = view.getAdapter();
            if (!(adapter instanceof  NewsOfflineRecyclerViewAdapter)) {
                adapter = new NewsOfflineRecyclerViewAdapter(refActivity.get());
                view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                view.setAdapter(adapter);
            }
            ((NewsOfflineRecyclerViewAdapter) adapter).setCursor(cursor);
            adapter.notifyDataSetChanged();
        }
    }

    @FunctionalInterface
    public interface QueryHelper {
        Cursor query();
    }
}
