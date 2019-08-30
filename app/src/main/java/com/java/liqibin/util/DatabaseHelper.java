package com.java.liqibin.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "news";

    public DatabaseHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (" +
                "_id integer primary key autoincrement, " +
                "newsID text unique, " +
                "category text, " +
                "image text, " +
                "title text, " +
                "publisher text, " +
                "publishTime text, " +
                "json text, " +
                "favored integer" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
