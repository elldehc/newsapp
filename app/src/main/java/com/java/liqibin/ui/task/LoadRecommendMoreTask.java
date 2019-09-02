package com.java.liqibin.ui.task;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.bean.NewsResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.List;

public class LoadRecommendMoreTask extends LoadRecommendNewsTask {
    private WeakReference<SmartRefreshLayout> refLayout;

    public LoadRecommendMoreTask(Activity activity, RecyclerView view, SmartRefreshLayout smartRefreshLayout) {
        super(activity, view);
        refLayout = new WeakReference<>(smartRefreshLayout);
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);
        SmartRefreshLayout smartRefreshLayout = refLayout.get();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMore(response != null);
        }
    }
}
