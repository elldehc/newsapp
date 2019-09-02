package com.java.liqibin.ui.news;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;
import com.java.liqibin.ui.task.LoadMoreTask;
import com.java.liqibin.ui.task.OfflineLoadMoreTask;
import com.java.liqibin.ui.task.OfflineLoadNewsTask;
import com.java.liqibin.ui.task.OfflineRefreshTask;
import com.java.liqibin.ui.task.RefreshTask;
import com.java.liqibin.util.CheckNetworkState;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


public class FavouriteFragment extends Fragment {
    private Cursor cursor = null;
    private boolean offline = false;

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
        OfflineLoadNewsTask.QueryHelper queryHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    "favored = 1", null, null, null, null, "15");
            return cursor;
        };

        OfflineLoadNewsTask.QueryHelper loadMoreHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    "favored = 1", null, null, null, null,
                    Integer.toString(cursor.getCount() + 15));

            return cursor;
        };

        TextView showEmpty = view.findViewById(R.id.show_empty);
        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener((layout) -> {
                new OfflineRefreshTask(activity, newsList, showEmpty, queryHelper, (SmartRefreshLayout) layout).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });
        refreshLayout.setOnLoadMoreListener((layout) -> {
            new OfflineLoadMoreTask(activity, newsList, showEmpty, loadMoreHelper, (SmartRefreshLayout) layout).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });
        refreshLayout.autoRefresh(0, 0, 0, false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

}
