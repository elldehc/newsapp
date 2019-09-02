package com.java.liqibin.ui.main.fragment;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;
import com.java.liqibin.ui.main.MainActivity;
import com.java.liqibin.ui.task.LoadMoreTask;
import com.java.liqibin.ui.task.LoadNewsTask;
import com.java.liqibin.ui.task.OfflineLoadNewsTask;
import com.java.liqibin.util.CheckNetworkState;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import km.lmy.searchview.SearchView;

public class SearchResultFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView newsList;
    private SmartRefreshLayout refreshLayout;
    private TextView showEmpty;
    private SearchView searchView;

    private String keyword;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        toolbar = view.findViewById(R.id.search_result_toolbar);
        activity.setSupportActionBar(toolbar);

        searchView = view.findViewById(R.id.searchView);
        activity.setSearchView(searchView);
        activity.setupSearchView(searchView);

        newsList = view.findViewById(R.id.newsList);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        showEmpty = view.findViewById(R.id.no_result);
        String word = getArguments().getString("word");

        refreshLayout.setOnLoadMoreListener((layout) -> {
            if (CheckNetworkState.isNetwordConnected(activity)) {
                NewsRecyclerViewAdapter adapter = (NewsRecyclerViewAdapter) newsList.getAdapter();
                int page = adapter == null ? 1 : adapter.getCurrentPage() + 1;
                new LoadMoreTask(activity, newsList, showEmpty, (SmartRefreshLayout) layout)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new NewsQuery().setWords(keyword).setPage(page));
            } else {
                Toast.makeText(activity, "无法连接到网络！", Toast.LENGTH_SHORT).show();
                layout.finishLoadMore(false);
            }
        });
        search(word);
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void search(String s) {
        keyword = s;
        toolbar.setTitle("新闻搜索：" + s);
        SQLiteDatabase database = NewsDatabase.getWritable();
        database.execSQL("insert or replace into history (item) values ('" + s + "')");
        NewsRecyclerViewAdapter adapter = (NewsRecyclerViewAdapter) newsList.getAdapter();
        if (adapter != null) {
            adapter.clearNews();
        }
        if (CheckNetworkState.isNetwordConnected(getContext())) {
            new LoadNewsTask(getActivity(), newsList, showEmpty)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new NewsQuery().setWords(keyword));
        } else {
            Toast.makeText(getContext(), "无法连接到网络！", Toast.LENGTH_SHORT).show();
            showEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
}
