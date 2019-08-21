package com.java.liqibin.app;

import android.app.Application;

public class NewsApp extends Application {
    private static NewsApp app;

    public static synchronized NewsApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
