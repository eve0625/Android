package com.cindy.androidstudy.volley;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

public class HttpRequestQueue {

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private HttpRequestQueue(Context context) {
        mContext = context.getApplicationContext();

        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); //1MB
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    private static HttpRequestQueue instance;
    public static synchronized HttpRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequestQueue(context);
        }
        return instance;
    }

    public <T> void add(Request<T> request) {
        mRequestQueue.add(request);
    }

    public void cancelAll(Object TAG) {
        mRequestQueue.cancelAll(TAG);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
