package com.java.liqibin.ui.task;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.bean.NewsResponse;
import com.java.liqibin.model.http.NewsDownloader;
import com.java.liqibin.ui.adapter.NewsRecyclerViewAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class LoadRecommendNewsTask extends AsyncTask<NewsQuery, Void, Integer> {

    private WeakReference<Activity> refActivity;
    private WeakReference<RecyclerView> refView;
    private WeakReference<HashSet<String>> refBufferSet;
    private WeakReference<List<News>> refBuffer;

    public LoadRecommendNewsTask(Activity activity, RecyclerView view) {
        this.refActivity = new WeakReference<>(activity);
        this.refView = new WeakReference<>(view);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        RecyclerView view = refView.get();
        if (view != null) {
            RecyclerView.Adapter adapter = view.getAdapter();
            if (!(adapter instanceof  NewsRecyclerViewAdapter)) {
                adapter = new NewsRecyclerViewAdapter(refActivity.get());
                view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                view.setAdapter(adapter);
            }
            refBufferSet=new WeakReference<>(((NewsRecyclerViewAdapter) adapter).bufferSet);
            refBuffer=new WeakReference<>(((NewsRecyclerViewAdapter) adapter).buffer);
        }
    }

    @Override
    protected Integer doInBackground(NewsQuery... newsQueries) {
        HashSet<String> bufferSet=refBufferSet.get();
        List<News> buffer=refBuffer.get();
        Integer ans=0;
        while(buffer.size()<15)
        {
            ans++;
            for(int i=0;i<newsQueries.length;i++)if(newsQueries[i]!=null)
            {
                System.err.println(newsQueries[i].getWords());
                //buffer.addAll(NewsDownloader.fetch(newsQueries[i]).data);
                List<News> ret=NewsDownloader.fetch(newsQueries[i]).data;
                int t=ret.size();
                for(int j=0;j<t;j++)
                {
                    News tt=ret.get(j);
                    if(!bufferSet.contains(tt.newsID)) {
                        buffer.add(tt);
                        bufferSet.add(tt.newsID);
                        System.err.println(tt.newsID);
                    }
                }
            }
        }
        Collections.shuffle(buffer);
        return ans;
        //return NewsDownloader.fetch(newsQueries[0]);
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);
        RecyclerView view = refView.get();
        if (view != null) {
            RecyclerView.Adapter adapter = view.getAdapter();
            if (!(adapter instanceof  NewsRecyclerViewAdapter)) {
                adapter = new NewsRecyclerViewAdapter(refActivity.get());
                view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
                view.setAdapter(adapter);
            }
            ((NewsRecyclerViewAdapter) adapter).addAll(response);
            adapter.notifyDataSetChanged();
        }
    }
}
