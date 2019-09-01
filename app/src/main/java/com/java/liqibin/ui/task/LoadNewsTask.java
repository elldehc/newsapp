package com.java.liqibin.ui.task;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.bean.NewsResponse;
import com.java.liqibin.model.http.NewsDownloader;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;

import java.lang.ref.WeakReference;

public class LoadNewsTask extends AsyncTask<NewsQuery, Void, NewsResponse> {

    private WeakReference<Activity> refActivity;
    private WeakReference<RecyclerView> refView;

    public LoadNewsTask(Activity activity, RecyclerView view) {
        this.refActivity = new WeakReference<>(activity);
        this.refView = new WeakReference<>(view);
    }

    @Override
    protected NewsResponse doInBackground(NewsQuery... newsQueries) {
        return NewsDownloader.fetch(newsQueries[0]);
    }

    @Override
    protected void onPostExecute(NewsResponse response) {
        super.onPostExecute(response);
        RecyclerView view = refView.get();
        if (view != null) {
            RecyclerView.Adapter adapter = view.getAdapter();
            if (!(adapter instanceof  NewsRecyclerViewAdapter)) {
                adapter = new NewsRecyclerViewAdapter(refActivity.get());
                view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                view.setAdapter(adapter);
            }
            ((NewsRecyclerViewAdapter) adapter).addResponse(response);
            adapter.notifyDataSetChanged();
        }
    }
}
