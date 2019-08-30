package com.java.liqibin.ui.news;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.util.DownloadImageTask;

class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private Cursor cursor;

    NewsRecyclerViewAdapter(@NonNull Cursor cursor) {
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
            cursor.moveToPosition(position);
            String url = cursor.getString(cursor.getColumnIndex("image"));
            url = url.substring(1, url.length() - 1).split(",")[0];
            if (url.length() > 0) {
                new DownloadImageTask(holder.newsIcon).execute(url);
            } else {
                holder.newsIcon.setMaxHeight(0);
            }
            holder.newsTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
            holder.newsPublisher.setText(cursor.getString(cursor.getColumnIndex("publisher")));
            holder.newsTime.setText(cursor.getString(cursor.getColumnIndex("publishTime")));
            holder.isAdded = true;
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsIcon;
        TextView newsTitle;
        TextView newsPublisher;
        TextView newsTime;
        boolean isAdded;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsIcon = itemView.findViewById(R.id.newsIcon);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsPublisher = itemView.findViewById(R.id.newsPublisher);
            newsTime = itemView.findViewById(R.id.newsTime);
            isAdded = false;
        }
    }
}
