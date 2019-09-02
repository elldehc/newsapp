package com.java.liqibin.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.NewsActivity;
import com.java.liqibin.R;
import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.bean.NewsResponse;
import com.java.liqibin.model.db.NewsDatabase;
import com.java.liqibin.ui.task.DownloadImageTask;
import com.java.liqibin.ui.task.LoadRecommendNewsTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private Activity activity;
    private List<News> data;
    private int currentPage;
    public LinkedList<News> buffer;
    public HashSet<String> bufferSet;

    public NewsRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
        this.data = new ArrayList<>();
        buffer=new LinkedList<>();
        bufferSet=new HashSet<>();
        currentPage = 0;
    }
    public int getBufferSize(){return buffer.size();}

    public void addResponse(NewsResponse response) {
        data.addAll(response.data);
        currentPage++;
    }
    public void addAll(Integer response) {
        for(int i=0;i<15;i++)data.add(buffer.pop());
        currentPage+=response;
    }
    public void clearNews() {
        data.clear();
        currentPage = 0;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_listitem, parent, false);
        return new NewsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = data.get(position);
        if (!news.newsID.equals(holder.newsID)) {
            holder.addNews(news);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsIcon;
        TextView newsTitle;
        TextView newsPublisher;
        TextView newsTime;
        String newsID;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIcon = itemView.findViewById(R.id.newsIcon);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsPublisher = itemView.findViewById(R.id.newsPublisher);
            newsTime = itemView.findViewById(R.id.newsTime);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(activity, NewsActivity.class);
                String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
                String message = newsID;
                newsTitle.setTextColor(activity.getResources().getColor(R.color.gray));
                intent.putExtra(EXTRA_MESSAGE, message);
                activity.startActivity(intent);
            });
        }

        void addNews(News news) {
            String url = news.image;
            if (url.startsWith("[") && url.endsWith("]")) {
                url = url.substring(1, url.length() - 1).split(", ")[0];
            }
            if (url.length() > 0) {
                newsIcon.setImageResource(R.drawable.ic_action_waiting);
                new DownloadImageTask(newsIcon).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            } else {
                newsIcon.setImageResource(R.mipmap.ic_launcher);
            }
            newsTitle.setText(news.title);
            newsPublisher.setText(news.publisher);
            newsTime.setText(news.publishTime);
            newsID = news.newsID;
            SQLiteDatabase database = NewsDatabase.getReadable();
            Cursor cursor = database.query(NewsDatabase.TABLE_NAME, new String[]{"newsID", "lastview"},
                    "lastview is not null and newsID = ?", new String[]{newsID}, null, null, null);
            if (cursor.getCount() > 0) {
                newsTitle.setTextColor(activity.getResources().getColor(R.color.gray));
            }
        }
    }


}
