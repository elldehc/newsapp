package com.java.liqibin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView view;

    public DownloadImageTask(ImageView view) {
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URLConnection connection = new URL(strings[0]).openConnection();
            try (InputStream is = new BufferedInputStream(connection.getInputStream())) {
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        view.setImageBitmap(bitmap);
    }
}
