package com.contact.client;

import android.app.Application;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

/**
 * Static client for communicating with backend.
 * Created by rtteal on 11/16/2014.
 */
public class ContactClient extends Application {
    private static final String BASE_URL = "http://192.168.56.1:8080/contact/";
    private static final AsyncHttpClient client = new AsyncHttpClient();
    private static Context context;

    public void onCreate(){
        super.onCreate();
        ContactClient.context = getApplicationContext();
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler){
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void post(String url, StringEntity params, AsyncHttpResponseHandler handler){
        client.post(context, getAbsoluteUrl(url), params, "application/json", handler);
    }

    private static String getAbsoluteUrl(String url){
        return BASE_URL + url;
    }
}
