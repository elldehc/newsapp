package com.java.liqibin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.google.gson.Gson;
import com.java.liqibin.model.bean.News;
import com.java.liqibin.model.db.NewsDatabase;
import com.mob.MobSDK;
import com.stx.xhb.xbanner.XBanner;
import com.youngfeng.snake.annotations.EnableDragToClose;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

@EnableDragToClose
public class NewsActivity extends AppCompatActivity {
    static final String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
    News news;
    SQLiteDatabase database;
    Cursor cursor;
    int favored = 0;
    MenuItem addtofav;
    final ArrayList<String> images=new ArrayList<>(),titles=new ArrayList<>();
    long timenow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        timenow=System.currentTimeMillis()/1000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        database = NewsDatabase.getWritable();
        cursor = database.query(NewsDatabase.TABLE_NAME, new String[]{"newsID", "category", "image", "title", "publisher", "publishTime", "json","favored"},
                "newsID = '"+message+"'", null, null, null, null);
        cursor.moveToFirst();
        favored=cursor.getInt(7);
        database.execSQL("insert or replace into " + NewsDatabase.TABLE_NAME +
                " (newsID, category, image, title, publisher, publishTime, json, favored, lastview) values (" +
                "'" + cursor.getString(0) + "', " +
                "'" + cursor.getString(1) + "', " +
                "'" + cursor.getString(2) + "', " +
                "'" + cursor.getString(3) + "', " +
                "'" + cursor.getString(4) + "', " +
                "'" + cursor.getString(5) + "', " +
                "'" + cursor.getString(6) + "', " +
                favored +", "+
                timenow+
                ");");

        Gson gson=new Gson();
        news=gson.fromJson(cursor.getString(6), News.class);
        TextView view1=findViewById(R.id.title);
        view1.setText(news.title);
        TextView view4=findViewById(R.id.content);
        view4.setText(news.content);
        if(news.image.length()>2) {
            String[] t=news.image.substring(1,news.image.length()-1).split("\\s*,\\s*");
            XBanner mXBanner = (XBanner) findViewById(R.id.xbanner);
            for(int i=0;i<t.length;i++) {
                images.add(t[i]);
                titles.add("");

            }
            mXBanner.setData(images, titles);
            mXBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    System.err.println(images.get(position));
                    Glide.with(getApplicationContext()).load(Uri.parse(images.get(position))).error(R.drawable.ic_launcher_foreground).transform(new CenterCrop()).into((ImageView) view);
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
                database.execSQL("insert or replace into " + NewsDatabase.TABLE_NAME +
                        " (newsID, category, image, title, publisher, publishTime, json, favored, lastview) values (" +
                        "'" + cursor.getString(0) + "', " +
                        "'" + cursor.getString(1) + "', " +
                        "'" + cursor.getString(2) + "', " +
                        "'" + cursor.getString(3) + "', " +
                        "'" + cursor.getString(4) + "', " +
                        "'" + cursor.getString(5) + "', " +
                        "'" + cursor.getString(6) + "', " +
                        (favored==0?"1, ":"0, ") +
                        timenow+
                        ");");
                favored=1-favored;
                addtofav.setTitle(getString(favored==1?R.string.deletefromfav:R.string.addtofav));
                Toast.makeText(getApplicationContext(), ""+favored, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.share_qq:
                showShare(QQ.NAME);
                return true;
            case R.id.share_wx:
                showShare(Wechat.NAME);
                return true;
            case R.id.share_wxpyq:
                showShare(WechatMoments.NAME);
                return true;
            case R.id.share_qzone:
                showShare(QZone.NAME);
                return true;
            case R.id.toggleday:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES?AppCompatDelegate.MODE_NIGHT_NO:AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                //showAnimation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setTitle(news.title);
        oks.setTitleUrl("http://sharesdk.cn");
        oks.setText(news.content);
        if(images.size()>0)oks.setImageUrl(images.get(0));
        oks.setUrl("http://sharesdk.cn");
        oks.show(MobSDK.getContext());
    }

}
