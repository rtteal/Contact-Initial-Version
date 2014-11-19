package com.contact.client;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by rtteal on 11/16/2014.
 */
public class ContactClient {
    private final String BASE_URL = "http://192.168.56.1:8080/contact/user/addressBook/taylor";
    private AsyncHttpClient client;

    public ContactClient(){
        client = new AsyncHttpClient();
    }

    public void getContacts(JsonHttpResponseHandler handler){
        //RequestParams params = new RequestParams("apikey", API_KEY);
        Log.d("debug", "creating connection...");
        client.get(BASE_URL, handler);
    }
}
