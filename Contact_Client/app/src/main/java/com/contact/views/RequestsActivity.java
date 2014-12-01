package com.contact.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contact.R;
import com.contact.adapter.ContactAdapter;
import com.contact.client.ContactClient;
import com.contact.model.Contact;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This activity allows the user to accept or decline
 * requests to connect.
 * Created by rtteal on 11/23/2014.
 */
public class RequestsActivity extends Activity {
    private ListView lvContacts;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(this, contactList);
        lvContacts.setAdapter(contactAdapter);
        fetchContacts();
        setupContactSelectedListener();
    }

    private void fetchContacts() {
        ContactClient.get("incoming-requests/taylor", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject body) {
                try {
                    JSONArray items = body.getJSONArray("requests");
                    ArrayList<Contact> contacts = Contact.fromJson(items);
                    for (Contact contact : contacts) {
                        contactAdapter.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                Log.e("onFailure: ", "error reaching endpoint", e);
            }
        });
    }

    public void setupContactSelectedListener() {
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Launch the detail view passing movie as an extra
                Intent i = new Intent(RequestsActivity.this, RequestDetailActivity.class);
                i.putExtra(ContactActivity.CONTACT_KEY, contactAdapter.getItem(position));
                startActivity(i);
            }
        });
    }
}
