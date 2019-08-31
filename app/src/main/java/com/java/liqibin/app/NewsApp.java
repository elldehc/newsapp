package com.java.liqibin.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.java.liqibin.model.db.NewsDatabase;
import com.youngfeng.snake.Snake;

public class NewsApp extends Application {
    private static NewsApp app;

    public static synchronized NewsApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Snake.init(this);
        NewsDatabase.newInstance(this);
    }

//    public SQLiteDatabase getWritableDatabase() {
//        return newsDatabase.getWritableDatabase();
//    }
//
//    public SQLiteDatabase getReadableDatabase() {
//        return newsDatabase.getReadableDatabase();
//    }
}
