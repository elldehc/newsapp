package com.java.liqibin.ui.task;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;

public class RefreshTask extends LoadNewsTask {
    private WeakReference<SmartRefreshLayout> layout;

    public RefreshTask(Activity activity, RecyclerView recyclerView, QueryHelper helper, SmartRefreshLayout layout) {
        super(activity, recyclerView, helper);
        this.layout = new WeakReference<>(layout);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        SmartRefreshLayout l = layout.get();
        if(l != null) {
            l.finishRefresh(success);
        }
    }
}
