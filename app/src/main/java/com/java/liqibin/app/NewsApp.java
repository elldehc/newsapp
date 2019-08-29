package com.java.liqibin.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.java.liqibin.util.DatabaseHelper;

public class NewsApp extends Application {
    private static NewsApp app;

    private static DatabaseHelper databaseHelper;

    public static synchronized NewsApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        databaseHelper = new DatabaseHelper(this, "news.db");
    }

    public SQLiteDatabase getWritableDatabase() {
        return databaseHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return databaseHelper.getReadableDatabase();
    }
}
