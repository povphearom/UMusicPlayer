package com.phearom.api.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetUtils {
    private static NetUtils instance;
    private Context mContext;

    private NetUtils(Context context) {
        this.mContext = context;
    }

    public static NetUtils init(Context context) {
        if (null == instance)
            instance = new NetUtils(context);
        return instance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}