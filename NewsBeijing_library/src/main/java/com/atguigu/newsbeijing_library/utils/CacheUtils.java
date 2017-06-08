package com.atguigu.newsbeijing_library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by My on 2017/6/2.
 */

public class CacheUtils {

    public static void putBoolean(Context context, String key, boolean b) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,b).commit();
    }


    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    public static String getString(Context context, String key) {
        String values = "";
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        values = sp.getString(key,"");

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            try {
                String dir = Environment.getExternalStorageDirectory()+"/beijingnews/files";
                String fileName = MD5Encoder.encode(key);
                File file = new File(dir, fileName);

                if(file.exists()){


                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((length=fis.read(buffer)) != -1){
                        baos.write(buffer,0,length);
                    }

                    String content = baos.toString();

                    if(!TextUtils.isEmpty(content)){
                        values = content;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return values;

    }

    public static void putString(Context context, String key, String values) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){


            try {
                String dir = Environment.getExternalStorageDirectory()+"/beijingnews/files";
                String fileName = MD5Encoder.encode(key);
                File file = new File(dir, fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(values.getBytes());
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
