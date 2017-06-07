package com.atguigu.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by My on 2017/6/7.
 */

public class NetCachUtils {
    public static final int SUCESS = 1;
    public static final int FAIL = 2;
    private final Handler handler;

    public NetCachUtils(Handler handler) {
        this.handler = handler;
    }


    public void getBitmapFromNet(String imageUrl, int position) {
        new Thread(new MyRunnable(imageUrl,position)).start();
    }

    class MyRunnable implements Runnable{

        private final String imageUrl;
        private final int position;

        public MyRunnable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);
                int code = urlConnection.getResponseCode();
                if(200 == code){
                    InputStream is = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    msg.what = SUCESS;
                    msg.arg1 = position;
                    handler.sendMessage(msg);


                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }

        }
    }
}