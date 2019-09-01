package com.java.liqibin.ui.news;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.bean.DateTime;
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


public class ColumnFragment extends Fragment {
    private String[] columnName;
    private Cursor cursor;
    private boolean offline = false;

    public ColumnFragment(String columnName) {
        this.columnName = new String[]{columnName};
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

        OfflineLoadNewsTask.QueryHelper queryHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    "category=?", columnName, null, null, null, "15");
            return cursor;
        };

        OfflineLoadNewsTask.QueryHelper loadMoreHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    "category=?", columnName, null, null, null,
                    Integer.toString(cursor.getCount() + 15));
            return cursor;
        };

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener((layout) -> {
            if (CheckNetworkState.isNetwordConnected(activity)) {
                offline = false;
                new RefreshTask(activity, newsList, (SmartRefreshLayout) layout)
                        .execute(new NewsQuery().setCategories(columnName[0]));
            } else {
                offline = true;
                Toast.makeText(activity, "无法连接到网络，将加载离线新闻！", Toast.LENGTH_SHORT).show();
                new OfflineRefreshTask(activity, newsList, queryHelper, (SmartRefreshLayout) layout).execute();
            }
        });
        refreshLayout.setOnLoadMoreListener((layout) -> {
            if (CheckNetworkState.isNetwordConnected(activity)) {
                if (offline) {
                    Toast.makeText(activity, "回到顶部刷新即可获取在线新闻！\n继续加载离线新闻...", Toast.LENGTH_SHORT).show();
                    new OfflineLoadMoreTask(activity, newsList, loadMoreHelper, (SmartRefreshLayout) layout).execute();
                } else {
                    NewsRecyclerViewAdapter adapter = (NewsRecyclerViewAdapter) newsList.getAdapter();
                    int page = adapter == null ? 1 : adapter.getCurrentPage() + 1;
                    new LoadMoreTask(activity, newsList, (SmartRefreshLayout) layout)
                            .execute(new NewsQuery().setCategories(columnName[0]).setPage(page));
                }
            } else {
                if (!offline) {
                    Toast.makeText(activity, "无法连接到网络，回到顶部刷新可获取离线新闻！", Toast.LENGTH_SHORT).show();
                    layout.finishLoadMore(false);
                } else {
                    new OfflineLoadMoreTask(activity, newsList, loadMoreHelper, (SmartRefreshLayout) layout).execute();
                }
            }
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
