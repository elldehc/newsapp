package com.java.liqibin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.java.liqibin.app.NewsApp;
import com.java.liqibin.util.DatabaseHelper;
import com.squareup.picasso.Picasso;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;

class Keyword
{
    double score;
    String word;
};
class Person
{
    int count;
    String linkedURL,mention;
};
class Location
{
    int count;
    double lng,lat;
    String linkedURL,mention;
};
class NewsData
{
    String image,publishTime,language,video,title,content,newsID,crawlTime,publisher,category;
    Keyword[] keywords,when,where,who;
    Person[] persons,organizations;
    Location[] locations;
};
public class NewsActivity extends AppCompatActivity {
    static final String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
    NewsData news;
    SQLiteDatabase database;
    Cursor cursor;
    int favored;
    MenuItem addtofav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        database = NewsApp.getApp().getWritableDatabase();
        cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{"newsID", "category", "image", "title", "publisher", "publishTime", "json", "favored"},
                "newsID = '"+message+"'", null, null, null, null);
        cursor.moveToFirst();
        Gson gson=new Gson();
        news=gson.fromJson(cursor.getString(6),NewsData.class);
        TextView view1=findViewById(R.id.textView);
        view1.setText(news.title);
        TextView view4=findViewById(R.id.textView4);
        view4.setText(news.content);
        favored=cursor.getInt(7);
        if(news.image.length()>2) {
            String[] t=news.image.substring(1,news.image.length()-1).split(",");
            XBanner mXBanner = (XBanner) findViewById(R.id.xbanner);
            final ArrayList<String> images=new ArrayList<>(),titles=new ArrayList<>();
            for(int i=0;i<t.length;i++) {
                images.add(t[i]);
                titles.add("");

            }
            mXBanner.setData(images, titles);
            mXBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {

                    Picasso.get().load(images.get(position)).resize(800, 600).into((ImageView) view);
                }
            });
//            ImageView iview = findViewById(R.id.imageView2);
//            Picasso.get().load(news.image.substring(1, news.image.length() - 1)).resize(800,600).into(iview);
        }
        if(!news.video.equals(""))
        {
            VideoView vview=findViewById(R.id.videoView2);
            //vview.setVideoPath(news.video);
            vview.setVideoURI(Uri.parse(news.video));
            vview.setMediaController(new MediaController(this));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        addtofav = menu.findItem(R.id.addtofav);
        addtofav.setTitle(getString(favored==1?R.string.deletefromfav:R.string.addtofav));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newsmenu, menu);
        addtofav = menu.findItem(R.id.addtofav);
        addtofav.setTitle(getString(favored==1?R.string.deletefromfav:R.string.addtofav));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.share:
//                return true;
            case R.id.addtofav:
                database.execSQL("insert or replace into " + DatabaseHelper.TABLE_NAME +
                        " (newsID, category, image, title, publisher, publishTime, json, favored) values (" +
                        "'" + cursor.getString(0) + "', " +
                        "'" + cursor.getString(1) + "', " +
                        "'" + cursor.getString(2) + "', " +
                        "'" + cursor.getString(3) + "', " +
                        "'" + cursor.getString(4) + "', " +
                        "'" + cursor.getString(5) + "', " +
                        "'" + cursor.getString(6) + "', " +
                        (favored==0?"1":"0") +
                        ");");
                favored=1-favored;
                addtofav.setTitle(getString(favored==1?R.string.deletefromfav:R.string.addtofav));
                Toast.makeText(getApplicationContext(), ""+favored, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.share_qq:
                return true;
            case R.id.share_wx:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
