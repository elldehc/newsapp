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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.db.NewsDatabase;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    static final String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
    News news;
    SQLiteDatabase database;
    Cursor cursor;
    int favored = 0;
    MenuItem addtofav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        database = NewsDatabase.getWritable();
        cursor = database.query(NewsDatabase.TABLE_NAME, new String[]{"newsID", "category", "image", "title", "publisher", "publishTime", "json"},
                "newsID = '"+message+"'", null, null, null, null);
        cursor.moveToFirst();
        Gson gson=new Gson();
        news=gson.fromJson(cursor.getString(6), News.class);
        TextView view1=findViewById(R.id.title);
        view1.setText(news.title);
        TextView view4=findViewById(R.id.content);
        view4.setText(news.content);
        if(news.image.length()>2) {
            String[] t=news.image.substring(1,news.image.length()-1).split("\\s*,\\s*");
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
                    System.err.println(images.get(position));
                    Glide.with(getApplicationContext()).load(Uri.parse(images.get(position))).error(R.drawable.ic_launcher_foreground).into((ImageView) view);
                    //Picasso.get().load(images.get(position))/*.resize(800, 600)*/.error(R.drawable.ic_launcher_foreground).into((ImageView) view);
                }
            });
//            ImageView iview = findViewById(R.id.imageView2);
//            Picasso.get().load(news.image.substring(1, news.image.length() - 1)).resize(800,600).into(iview);
        }
        else
        {
            XBanner mXBanner = (XBanner) findViewById(R.id.xbanner);
            mXBanner.setVisibility(View.GONE);
        }
        if(!news.video.equals(""))
        {
            VideoView vview=findViewById(R.id.videoView2);
            //vview.setVideoPath(news.video);
            vview.setVideoURI(Uri.parse(news.video));
            vview.setMediaController(new MediaController(this));
        }
        else
        {
            VideoView vview=findViewById(R.id.videoView2);
            vview.setVisibility(View.GONE);
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
//                database.execSQL("insert or replace into " + NewsDatabase.TABLE_NAME +
//                        " (newsID, category, image, title, publisher, publishTime, json, favored) values (" +
//                        "'" + cursor.getString(0) + "', " +
//                        "'" + cursor.getString(1) + "', " +
//                        "'" + cursor.getString(2) + "', " +
//                        "'" + cursor.getString(3) + "', " +
//                        "'" + cursor.getString(4) + "', " +
//                        "'" + cursor.getString(5) + "', " +
//                        "'" + cursor.getString(6) + "', " +
//                        (favored==0?"1":"0") +
//                        ");");
//                favored=1-favored;
//                addtofav.setTitle(getString(favored==1?R.string.deletefromfav:R.string.addtofav));
//                Toast.makeText(getApplicationContext(), ""+favored, Toast.LENGTH_SHORT).show();
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
