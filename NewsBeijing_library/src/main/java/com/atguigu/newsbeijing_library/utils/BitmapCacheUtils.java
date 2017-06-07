package com.atguigu.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by My on 2017/6/7.
 */

public class BitmapCacheUtils {
    private NetCachUtils netCachUtils;

    public BitmapCacheUtils(Handler handler){
        netCachUtils = new NetCachUtils(handler);
    }

    public Bitmap getBitmap(String imageUrl, int position) {
        netCachUtils.getBitmapFromNet(imageUrl,position);

        return null;
    }
}
