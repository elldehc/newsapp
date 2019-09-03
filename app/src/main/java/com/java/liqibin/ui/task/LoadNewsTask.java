package com.java.liqibin.ui.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.bean.NewsResponse;
import com.java.liqibin.model.http.NewsDownloader;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;

import java.lang.ref.WeakReference;

public class LoadNewsTask extends AsyncTask<NewsQuery, Void, NewsResponse> {

    private WeakReference<Activity> refActivity;
    protected WeakReference<RecyclerView> refView;
    private WeakReference<TextView> refShowEmpty;

    public LoadNewsTask(Activity activity, RecyclerView view, TextView showEmpty) {
        this.refActivity = new WeakReference<>(activity);
        this.refView = new WeakReference<>(view);
        this.refShowEmpty = new WeakReference<>(showEmpty);
    }

    @Override
    protected NewsResponse doInBackground(NewsQuery... newsQueries) {
        return NewsDownloader.fetch(newsQueries[0]);
    }

    @Override
    protected void onPostExecute(NewsResponse response) {
        super.onPostExecute(response);
        if (response != null) {
            RecyclerView view = refView.get();
            if (view != null) {
                RecyclerView.Adapter adapter = view.getAdapter();
                if (!(adapter instanceof NewsRecyclerViewAdapter)) {
                    adapter = new NewsRecyclerViewAdapter(refActivity.get());
                    view.setAdapter(adapter);
                }
                ((NewsRecyclerViewAdapter) adapter).addResponse(response);
                TextView showEmpty = refShowEmpty.get();
                if (showEmpty != null) {
                    showEmpty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
