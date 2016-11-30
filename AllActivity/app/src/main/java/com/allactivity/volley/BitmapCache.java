package com.allactivity.volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 张继 on 2016/11/22.
 * 缓存
 */

public class BitmapCache implements ImageLoader.ImageCache {
    public LruCache<String,Bitmap> mLruCache;
    public int max=10*1024*1024;

    public BitmapCache() {
        mLruCache=new LruCache<String, Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mLruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mLruCache.put(s,bitmap);
    }
}
