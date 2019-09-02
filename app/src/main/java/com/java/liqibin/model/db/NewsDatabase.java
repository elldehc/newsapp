package com.java.liqibin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "news";

    private static final String[] columns = {"娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技", "社会"};

    public static NewsDatabase database;

    private NewsDatabase(@Nullable Context context) {
        super(context, "news.db", null, VERSION);
    }

    public static void newInstance(@Nullable Context context) {
        database = new NewsDatabase(context);
    }

    @NonNull
    public static SQLiteDatabase getReadable() {
        return database.getReadableDatabase();
    }

    @NonNull
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
                "json text," +
                "favored integer," +
                "lastview integer" +
                ");";
        db.execSQL(sql);
        sql = "create table if not exists categories (" +
                "id integer primary key autoincrement, " +
                "name text not null, " +
                "position integer not null" +
                ");";
        db.execSQL(sql);
        for (int i = 0; i < columns.length; i++) {
            db.execSQL("insert into categories (name, position) values ('" + columns[i] + "', " + i + ")");
        }

        db.execSQL("create table if not exists history (" +
                "id integer primary key autoincrement, " +
                "item text unique);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
