package com.java.liqibin.model.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "localuser";

    public static UserDatabase database;

    private UserDatabase(@Nullable Context context) {
        super(context, "news.db", null, VERSION);
    }

    public static void  newInstance(@Nullable Context context) {
        database = new UserDatabase(context);
    }

    public static SQLiteDatabase getReadable() {
        return database.getReadableDatabase();
    }

    public static SQLiteDatabase getWritable() {
        return database.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " (" +
                "newsID text primary key, " +
                "category text, " +
                "image text, " +
                "title text, " +
                "publisher text, " +
                "publishTime text, " +
                "json text" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
