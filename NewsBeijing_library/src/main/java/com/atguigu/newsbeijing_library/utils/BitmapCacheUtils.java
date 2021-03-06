package com.atguigu.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * Created by My on 2017/6/7.
 */

public class BitmapCacheUtils {
    private NetCachUtils netCachUtils;
    private LocalCachUtils localCachUtils;
    private MemoryCachUtils memoryCachUtils;

    public BitmapCacheUtils(Handler handler){
        memoryCachUtils = new MemoryCachUtils();
        localCachUtils = new LocalCachUtils(memoryCachUtils);
        netCachUtils = new NetCachUtils(handler, localCachUtils,memoryCachUtils);
    }

    public Bitmap getBitmap(String imageUrl, int position) {
        netCachUtils.getBitmapFromNet(imageUrl,position);
        if (localCachUtils != null){
            Bitmap bitmap = localCachUtils.getBitmap(imageUrl);
            if (bitmap != null) {
                Log.e("TAG", "图片是从本地获取的==" + position);
                return bitmap;
            }
        }
        netCachUtils.getBitmapFromNet(imageUrl, position);

        return null;
    }
}
