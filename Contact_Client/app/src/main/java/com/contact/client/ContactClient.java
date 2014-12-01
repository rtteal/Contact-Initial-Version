package com.contact.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Static client for communicating with backend.
 * Created by rtteal on 11/16/2014.
 */
public class ContactClient extends Application {
    private static final String BASE_URL = "http://192.168.56.1:8080/contact/";
    private static final AsyncHttpClient client = new AsyncHttpClient();
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        ContactClient.context = getApplicationContext();
        client.setTimeout(10000);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler){
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void post(String url, JSONObject json, AsyncHttpResponseHandler handler){
        try {
            StringEntity entity = new StringEntity(json.toString());
            client.post(context, getAbsoluteUrl(url), entity, "application/json", handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String getAbsoluteUrl(String url){
        return BASE_URL + url;
    }
}
