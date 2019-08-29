package com.java.liqibin.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "news";

    public DatabaseHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (" +
                "id integer primary key, " +
                "image text, " +
                "publishTime text, " +
                "keywords text, " +
                "video text, " +
                "title text, " +
                "_when text, " +
                "content text, " +
                "persons text, " +
                "newsID text, " +
                "crawlTime text, " +
                "organizations text, " +
                "publisher text, " +
                "locations text, " +
                "_where text, " +
                "category text," +
                "who text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
