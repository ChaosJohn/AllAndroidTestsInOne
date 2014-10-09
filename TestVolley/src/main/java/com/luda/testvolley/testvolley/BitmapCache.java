package com.luda.testvolley.testvolley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by chaos on 14-10-9.
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> lruCache;

    public BitmapCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 4;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return lruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        lruCache.put(s, bitmap);
    }
}
