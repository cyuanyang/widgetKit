package com.tal.imagepicker.utils;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.LruCache;

/**
 * Created by cyy on 2016/7/4.
 * 图片缓存
 */
public class ImageCache {

    private static ImageCache cache;

    private LruCache<String , Bitmap> mLruCache;

    public static ImageCache getInstance(){
        if (cache== null){
            synchronized (ImageCache.class){
                if (cache == null){
                    cache = new ImageCache();
                }
            }
        }
        return cache;
    }

    private ImageCache(){
        init();
    }

    private void init() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        mLruCache = new LruCache<String, Bitmap>((int) maxMemory/8 ){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void put(String key , Bitmap bm){
        if (bm == null)return;
        mLruCache.put(key , bm);
    }

    public @Nullable
    Bitmap get(String key ){
       return mLruCache.get(key);
    }

}
