package com.java.liqibin.ui.task;

import android.app.Activity;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.NewsResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;

public class RefreshTask extends LoadNewsTask {
    private WeakReference<SmartRefreshLayout> refLayout;

    public RefreshTask(Activity activity, RecyclerView recyclerView, TextView showEmpty, SmartRefreshLayout smartRefreshLayout) {
        super(activity, recyclerView, showEmpty);
        this.refLayout = new WeakReference<>(smartRefreshLayout);
    }

    @Override
    protected void onPostExecute(NewsResponse response) {
        super.onPostExecute(response);
        SmartRefreshLayout smartRefreshLayout = refLayout.get();
        if(smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(response != null);
        }
    }
}
