package com.phearom.um.server;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phearom.um.config.Config;
import com.phearom.um.model.MusicProviderSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phearom on 7/18/16.
 */
public abstract class ServerRequest implements MusicProviderSource, Response.ErrorListener, Response.Listener<String> {
    private List<MediaMetadataCompat> metadataCompatList;
    private static RequestQueue mQueue;
    private Context mContext;
    private OnResultCallback<String> onResultCallback;

    public ServerRequest(Context context) {
        mContext = context;
        metadataCompatList = new ArrayList<>();
    }

    public RequestQueue getQueue() {
        if (null == mQueue)
            mQueue = Volley.newRequestQueue(mContext);
        return mQueue;
    }

    public abstract String getFunctionName();

    private String getServer() {
        return Config.AUTH.SERVER_IP + Config.SERVICE + getFunctionName();
    }

    public Map<String, String> getParameter(Map<String, String> params) {
        return params;
    }

    protected void execute() {
        StringRequest request = new StringRequest(Request.Method.POST, getServer(), this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return getParameter(params);
            }
        };
        getQueue().add(request);
    }

    protected void execute(OnResultCallback<String> callback) {
        setOnResultCallback(callback);
        StringRequest request = new StringRequest(Request.Method.POST, getServer(), this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return getParameter(params);
            }
        };
        getQueue().add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null != getOnResultCallback())
            getOnResultCallback().onError(error);
        getQueue().stop();
    }

    @Override
    public void onResponse(String response) {
        if (null != getOnResultCallback())
            getOnResultCallback().onResult(response);
        getQueue().stop();
    }

    public List<MediaMetadataCompat> getMetadataCompatList() {
        return metadataCompatList;
    }

    public void setMetadataCompatList(List<MediaMetadataCompat> metadataCompatList) {
        this.metadataCompatList = metadataCompatList;
    }

    public OnResultCallback<String> getOnResultCallback() {
        return onResultCallback;
    }

    public void setOnResultCallback(OnResultCallback<String> onResultCallback) {
        this.onResultCallback = onResultCallback;
    }
}