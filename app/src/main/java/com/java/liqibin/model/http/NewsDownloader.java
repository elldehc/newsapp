package com.java.liqibin.model.http;

import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.java.liqibin.app.NewsApp;
import com.java.liqibin.model.bean.DateTime;
import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.bean.NewsQuery;
import com.java.liqibin.model.bean.NewsResponse;
import com.java.liqibin.model.db.NewsDatabase;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class NewsDownloader {
    private static final String baseUrl = "https://api2.newsminer.net/svc/news/queryNewsList";

    public static NewsResponse fetch(NewsQuery query) {
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
                builder.append("categories=").append(categories).append('&');
            }

            int page = query.getPage();
            if (page != -1) {
                builder.append("page=").append(page).append('&');
            }

            builder.setLength(builder.length() - 1);  // pop

            URL url = new URL(builder.toString());
            URLConnection connection = url.openConnection();
            Scanner input = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            String json = input.next();

            Gson gson = new Gson();
            NewsResponse response = gson.fromJson(json, NewsResponse.class);

            SQLiteDatabase db = NewsDatabase.getWritable();
            for (News news : response.data) {
                db.execSQL("insert or ignore into " + NewsDatabase.TABLE_NAME +
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
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
