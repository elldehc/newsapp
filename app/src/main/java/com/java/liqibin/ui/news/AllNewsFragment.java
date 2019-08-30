package com.java.liqibin.ui.news;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.java.liqibin.R;
import com.java.liqibin.app.NewsApp;
import com.java.liqibin.util.DatabaseHelper;
import com.java.liqibin.util.DateTime;
import com.java.liqibin.util.DownloadImageTask;
import com.java.liqibin.util.GetNews;
import com.java.liqibin.util.NewsQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllNewsFragment extends Fragment {
    private AppCompatActivity activity;
    private ListView newsList;

    private List<Map<String, Object>> listItem;

    private List<Map<String, Object>> nextBatch;
    private int startIndex;
    private int maxCount;
    private int total;

    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_allnews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listItem = new ArrayList<>();
        nextBatch = new ArrayList<>();

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
            return GetNews.search(new NewsQuery().setWords("谷歌").setEndDate(DateTime.now()));
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                SQLiteDatabase database = NewsApp.getApp().getReadableDatabase();
                cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", "image", "title", "publisher"},
                        null, null, null, null, null);
                if (adapter == null) {
                    adapter = new SimpleCursorAdapter(activity, R.layout.news_listitem, cursor,
                            new String[]{"image", "title", "publisher"},
                            new int[]{R.id.newsIcon, R.id.newsTitle, R.id.newsPublisher}, 0) {
                        @Override
                        public void setViewImage(ImageView v, String value) {
                            super.setViewImage(v, value);
                            value = value.substring(1, value.length() - 1).split(",")[0];
                            if (value.length() > 0) {
//                                new DownloadImageTask(v).execute(value);
                            }
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            super.bindView(view, context, cursor);
                            view.setOnClickListener(view1 -> {
                                Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
                            });
                        }
                    };

                    newsList.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } else {

            }
        }
    }
}
