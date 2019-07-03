package com.example.androidenklima.webservice;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class CustomRequestQueue {

    private static CustomRequestQueue instance;
    private RequestQueue queue;
    private static Context context;

    private CustomRequestQueue(Context context) {
        this.context = context;
        this.queue = getRequestQueue();
    }

    public static CustomRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new CustomRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            queue = new RequestQueue(cache, network);
            queue.start();
        }
        return queue;
    }
}
