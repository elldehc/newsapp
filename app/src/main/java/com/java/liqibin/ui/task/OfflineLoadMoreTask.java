package com.java.liqibin.ui.task;

import android.app.Activity;
import android.database.Cursor;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;

public class OfflineLoadMoreTask extends OfflineLoadNewsTask {
    private WeakReference<SmartRefreshLayout> refLayout;

    public OfflineLoadMoreTask(Activity activity, RecyclerView recyclerView, TextView showEmpty, QueryHelper helper, SmartRefreshLayout layout) {
        super(activity, recyclerView, showEmpty, helper);
        this.refLayout = new WeakReference<>(layout);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        SmartRefreshLayout layout = refLayout.get();
        if (layout != null) {
            layout.finishLoadMore(true);
        }
    }
}
