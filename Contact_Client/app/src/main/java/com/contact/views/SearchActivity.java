package com.contact.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.contact.R;
import com.contact.client.ContactClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rtteal on 12/6/2014.
 */
public class SearchActivity extends Activity {
    Button btSubmit;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        etSearch = (EditText) findViewById(R.id.etSearch);
        createClickListener();
    }

    private void createClickListener(){
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestForContactInfo(etSearch.getText().toString());
                Intent i = new Intent(SearchActivity.this, ContactActivity.class);
                startActivity(i);
            }
        });
    }

    private void sendRequestForContactInfo(String userName){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userName", "taylor");
            jsonParams.put("otherUserName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String type = "request-contact";
        ContactClient.post(type, jsonParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s){
                Log.d(type, "success!");

            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.e(type, error.toString());
                // TODO generate failure message for user
            }

        });
    }
}
