package com.java.liqibin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.java.liqibin.main.MainActivity;
import com.squareup.picasso.Picasso;

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
    public static final String EXTRA_MESSAGE = "com.java.liqibin.NEWS_DETAIL";
    NewsData news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Gson gson=new Gson();
        news=gson.fromJson(message,NewsData.class);
        TextView view1=findViewById(R.id.textView);
        view1.setText(news.title);
        TextView view4=findViewById(R.id.textView4);
        view4.setText(news.content);
        if(!news.image.equals("[]")) {
            ImageView iview = findViewById(R.id.imageView2);
            Picasso.get().load(news.image.substring(1, news.image.length() - 1)).resize(800,600).into(iview);
        }
        if(!news.video.equals(""))
        {
            VideoView vview=findViewById(R.id.videoView2);
            vview.setVideoPath(news.video);
            vview.start();
        }
    }
}
