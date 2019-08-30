package com.java.liqibin.ui.news;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.liqibin.R;
import com.java.liqibin.app.NewsApp;
import com.java.liqibin.util.DatabaseHelper;
import com.java.liqibin.util.DateTime;
import com.java.liqibin.util.GetNews;
import com.java.liqibin.util.NewsQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllNewsFragment extends Fragment {
    private AppCompatActivity activity;

    private RecyclerView newsList;
    private NewsRecyclerViewAdapter adapter;
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_allnews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (AppCompatActivity) getActivity();
        assert activity != null;
        newsList = activity.findViewById(R.id.newsList);

        new LoadNewsTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }


    class LoadNewsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return GetNews.search(new NewsQuery().setEndDate(DateTime.now()));
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                SQLiteDatabase database = NewsApp.getApp().getReadableDatabase();
                cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", "image", "title", "publisher", "publishTime"},
                        null, null, null, null, null);
                if (adapter == null) {
                    adapter = new NewsRecyclerViewAdapter(cursor);
                    newsList.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(activity, "Failed to fetch news", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
