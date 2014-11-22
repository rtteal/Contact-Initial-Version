package com.contact.client;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by rtteal on 11/16/2014.
 */
public class ContactClient {
    private final String BASE_URL = "http://192.168.56.1:8080/contact/user/";
    private AsyncHttpClient client;

    public ContactClient(){
        client = new AsyncHttpClient();
    }

    public void getContacts(JsonHttpResponseHandler handler){
        //RequestParams params = new RequestParams("apikey", API_KEY);
        String url = BASE_URL + "addressBook/taylor";
        Log.d("getContacts", "creating connection to: " + url);
        client.get(url, handler);
    }

    public void getProfile(JsonHttpResponseHandler handler){
        //RequestParams params = new RequestParams("apikey", API_KEY);
        String url = BASE_URL + "profile/taylor";
        Log.d("getProfile", "creating connection to: " + url);
        client.get(url, handler);
    }
}
