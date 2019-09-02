package com.java.liqibin.ui.task;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.java.liqibin.R;
import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.adapter.NewsOfflineRecyclerViewAdapter;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;
import com.java.liqibin.ui.task.LoadMoreTask;
import com.java.liqibin.ui.task.OfflineLoadMoreTask;
import com.java.liqibin.ui.task.OfflineLoadNewsTask;
import com.java.liqibin.ui.task.OfflineRefreshTask;
import com.java.liqibin.ui.task.RefreshTask;
import com.java.liqibin.util.CheckNetworkState;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Math.log;

public class Recommender extends AsyncTask<Void, Void, String[]> {

    private WeakReference<View> refView;
    private WeakReference<Activity> refActivity;
    private Cursor cursor;
    private boolean offline = false;

    public Recommender(View view, Activity activity) {
        super();
        this.refView = new WeakReference<>(view);
        this.refActivity = new WeakReference<>(activity);
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        SQLiteDatabase database = NewsDatabase.getWritable();
        long timenow = System.currentTimeMillis() / 1000;
        cursor = database.query(NewsDatabase.TABLE_NAME, new String[]{"newsID", "keywords", "lastview"},
                "lastview >= " + (timenow - 3 * 86400), null, null, null, "lastview desc");
        HashMap<String, Double> mp = new HashMap<>();
        Gson gson = new Gson();
        for (; cursor.moveToNext(); ) {
            News news = new News();
            news.keywords = gson.fromJson(cursor.getString(1), (Type) News.Keyword[].class);
            for (int i = 0; i < news.keywords.length; i++) {
                if (mp.containsKey(news.keywords[i].word)) {
                    Double t = mp.get(news.keywords[i].word);
                    t += news.keywords[i].score * log(cursor.getInt(2) - (timenow - 3 * 86400) + 1);
                    mp.replace(news.keywords[i].word, t);
                } else {
                    Double t = news.keywords[i].score * log(cursor.getInt(2) - (timenow - 3 * 86400) + 1);
                    mp.put(news.keywords[i].word, t);
                }
            }
        }
        String[] ans = new String[6];
        Map.Entry[] tmp = new Map.Entry[mp.size()];
        mp.entrySet().toArray(tmp);
        Arrays.sort(tmp, new Comparator<Map.Entry>() {
            @Override
            public int compare(Map.Entry entry, Map.Entry t1) {
                return Double.compare((Double) entry.getValue(), (Double) t1.getValue());
            }
        });
        for (int i = 0; i < mp.size(); i++) {
            ans[i + 1] = (String) tmp[i].getKey();
            if (i == 4) break;
        }
        if (mp.size() > 0) {
            StringBuilder sb = new StringBuilder("keywords glob '*" + ans[1] + "*' ");
            for (int i = 2; i <= 5; i++)
                if (ans[i] != null) {
                    sb.append("or keywords glob '*");
                    sb.append(ans[i]);
                    sb.append("*' ");
                }
            ans[0] = sb.toString();
        }
        return ans;
    }


    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        View view = refView.get();
        Activity activity = refActivity.get();
        RecyclerView newsList = view.findViewById(R.id.newsList);

        TextView showEmpty = view.findViewById(R.id.show_empty);

        OfflineLoadNewsTask.QueryHelper queryHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    strings[0], null, null, null, null, "15");
            return cursor;
        };

        OfflineLoadNewsTask.QueryHelper loadMoreHelper = () -> {
            SQLiteDatabase database = NewsDatabase.getReadable();
            cursor = database.query(NewsDatabase.TABLE_NAME,
                    new String[]{"newsID", "image", "title", "publisher", "publishTime"},
                    strings[0], null, null, null, null,
                    Integer.toString(cursor.getCount() + 15));
            return cursor;
        };

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener((layout) -> {
            if (CheckNetworkState.isNetwordConnected(activity)) {
                offline = false;
                NewsQuery[] queries = new NewsQuery[5];
                for (int i = 1; i <= 5; i++)
                    if (strings[i] != null) {
                        queries[i - 1] = new NewsQuery().setWords(strings[i]);
                    }
                new RefreshRecommendTask(activity, newsList, (SmartRefreshLayout) layout).execute(queries);
            } else {
                offline = true;
                Toast.makeText(activity, "无法连接到网络，将加载离线新闻！", Toast.LENGTH_SHORT).show();
                new OfflineRefreshTask(activity, newsList, showEmpty, queryHelper, (SmartRefreshLayout) layout).execute();
            }
        });
        refreshLayout.setOnLoadMoreListener((layout) -> {
            if (CheckNetworkState.isNetwordConnected(activity)) {
                if (offline) {
                    Toast.makeText(activity, "回到顶部刷新即可获取在线新闻！\n继续加载离线新闻...", Toast.LENGTH_SHORT).show();
                    new OfflineLoadMoreTask(activity, newsList,showEmpty, loadMoreHelper, (SmartRefreshLayout) layout).execute();
                } else {
                    NewsRecyclerViewAdapter adapter = (NewsRecyclerViewAdapter) newsList.getAdapter();
                    int page = adapter == null ? 1 : adapter.getCurrentPage() + 1;
                    NewsQuery[] queries = new NewsQuery[5];
                    for (int i = 1; i <= 5; i++)
                        if (strings[i] != null) {
                            queries[i - 1] = new NewsQuery().setWords(strings[i]).setPage(page);
                        }
                    new LoadRecommendMoreTask(activity, newsList, (SmartRefreshLayout) layout).execute(queries);
                }
            } else {
                if (!offline) {
                    Toast.makeText(activity, "无法连接到网络，回到顶部刷新可获取离线新闻！", Toast.LENGTH_SHORT).show();
                    layout.finishLoadMore(false);
                } else {
                    new OfflineLoadMoreTask(activity, newsList, showEmpty, loadMoreHelper, (SmartRefreshLayout) layout).execute();
                }
            }
        });
        refreshLayout.autoRefresh(0, 0, 0, false);
        if (cursor != null) {
            cursor.close();
        }
    }
}
