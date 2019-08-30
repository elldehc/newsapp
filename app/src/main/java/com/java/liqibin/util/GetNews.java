package com.java.liqibin.util;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.gson.Gson;
import com.java.liqibin.app.NewsApp;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

public class GetNews {
    private static final String baseUrl = "https://api2.newsminer.net/svc/news/queryNewsList";

    public static boolean search(NewsQuery query) {
        StringBuilder builder = new StringBuilder(baseUrl);
        try {
            builder.append('?');

            int size = query.getSize();
            builder.append("size=").append(size).append('&');

            DateTime startDate = query.getStartDate();
            if (startDate != null) {
                builder.append("startDate=").append(startDate.toQueryString()).append('&');
            }

            DateTime endDate = query.getEndDate();
            if (endDate != null) {
                builder.append("endDate=").append(endDate.toQueryString()).append('&');
            }

            String words = query.getWords();
            if (words != null) {
                builder.append("words=").append(URLEncoder.encode(words, "UTF-8")).append('&');
            }

            String categories = query.getCategories();
            if (categories != null) {
                builder.append("categories=").append(URLEncoder.encode(categories, "UTF-8")).append('&');
            }

            builder.setLength(builder.length() - 1);  // pop

            URL url = new URL(builder.toString());
            URLConnection connection = url.openConnection();
            Scanner input = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            String json = input.next();

            Gson gson = new Gson();
            NewsResponse response = gson.fromJson(json, NewsResponse.class);

            SQLiteDatabase db = NewsApp.getApp().getWritableDatabase();
            for (NewsResponse.News news : response.data) {
                db.execSQL("insert or replace into " + DatabaseHelper.TABLE_NAME +
                        " (newsID, category, image, title, publisher, publishTime, json) values (" +
                        "'" + news.newsID + "', " +
                        "'" + news.category + "', " +
                        "'" + news.image + "', " +
                        "'" + news.title + "', " +
                        "'" + news.publisher + "', " +
                        "'" + news.publishTime + "', " +
                        "'" + gson.toJson(news) + "'" +
                        ");");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}