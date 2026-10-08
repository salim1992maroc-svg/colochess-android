package com.ctrange.colochess;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDex;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ctrange.colochess.tools.Constant;


public class App extends Application implements LifecycleObserver {

    private static App mInstance;
    private boolean isPlaying = false;
    public static final String TAG = App.class.getSimpleName();

    private RequestQueue mRequestQueue;

    public static String im = "img/";
    public static String ap = "api/";
    public static String ex = ".php";

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public static App getContext() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static String unt(){
        String baseUrl = BuildConfig.API_BASE_URL;
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            return "";
        }
        baseUrl = baseUrl.trim();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }




}
