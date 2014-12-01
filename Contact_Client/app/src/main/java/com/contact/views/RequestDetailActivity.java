package com.contact.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.contact.R;
import com.contact.client.ContactClient;
import com.contact.model.Contact;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rtteal on 11/16/2014.
 */
public class RequestDetailActivity extends Activity {
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;
    private Button btAccept, btDecline;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        // Fetch views
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btAccept = (Button) findViewById(R.id.btAccept);
        btDecline = (Button) findViewById(R.id.btDecline);
        // Use the movie to populate the data into our views
        contact = (Contact)
                getIntent().getSerializableExtra(ContactActivity.CONTACT_KEY);
        loadContact(contact);

        setUpOnClickListeners();
    }

    // Populate the data for the contact
    public void loadContact(Contact contact) {
        // Populate data
        tvName.setText(contact.getName());
        tvEmail.setText(contact.getEmail());
        tvPhone.setText(contact.getPhone());
        Picasso.with(this).load(contact.getPhoto()).
                placeholder(R.drawable.images).
                into(ivPhoto);
    }

    private void setUpOnClickListeners(){
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View item) {
                Log.d("accept-button", "begin");
                send("accept-contact");
                Intent i = new Intent(RequestDetailActivity.this, ContactActivity.class);
                startActivity(i);
            }
        });

        btDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View item) {
                Log.d("decline-button", "begin");
                send("decline-contact");
                Intent i = new Intent(RequestDetailActivity.this, ContactActivity.class);
                startActivity(i);
            }
        });
    }

    /*
     * Sends the Accept or Decline decision to persistence via API.
     */
    private void send(final String url){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userName", "taylor");
            jsonParams.put("otherUserName", contact.getUserName());
            ContactClient.post(url, jsonParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int code, JSONObject body) {
                    Log.d(url, "success");
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    Log.e(url, "failed", e);
                    // TODO generate failure message for user
                }

                @Override
                public void onFailure(Throwable e, JSONArray errorResponse) {
                    super.onFailure(e, errorResponse);
                    Log.e(url, "failed", e);
                    // TODO generate failure message for user
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
