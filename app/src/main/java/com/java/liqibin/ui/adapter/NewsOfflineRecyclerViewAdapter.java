package com.java.liqibin.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.NewsActivity;
import com.java.liqibin.R;

public class NewsOfflineRecyclerViewAdapter extends RecyclerView.Adapter<NewsOfflineRecyclerViewAdapter.NewsViewHolder> {

    private Activity activity;
    private Cursor cursor;

    public NewsOfflineRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_listitem, parent, false);
        return new NewsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if (!holder.isAdded) {
            synchronized (cursor) {
                cursor.moveToPosition(position);
                String url = cursor.getString(cursor.getColumnIndex("image"));
                url = url.substring(1, url.length() - 1).split(", ")[0];
                holder.newsIcon.setImageResource(R.mipmap.ic_launcher);
                holder.newsTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
                holder.newsPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher")));
                holder.newsTime.setText(cursor.getString(cursor.getColumnIndex("publishTime")));
                holder.newsID = cursor.getString(cursor.getColumnIndex("newsID"));
                holder.isAdded = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
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
        boolean isAdded;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIcon = itemView.findViewById(R.id.newsIcon);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsPublisher = itemView.findViewById(R.id.newsPublisher);
            newsTime = itemView.findViewById(R.id.newsTime);
            isAdded = false;

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(activity, NewsActivity.class);
                String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
                String message = newsID;
                intent.putExtra(EXTRA_MESSAGE, message);
                activity.startActivity(intent);
            });
        }
    }


}
