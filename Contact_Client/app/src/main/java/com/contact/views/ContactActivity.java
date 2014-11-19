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
 * Created by rtteal on 11/16/2014.
 */
public class ContactActivity extends Activity {
    private ListView lvContacts;
    private ContactAdapter contactAdapter;
    private ContactClient client;
    public static final String CONTACT_KEY = "contact";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lvContacts = (ListView) findViewById(R.id.lvContacts);
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(this, contactList);
        lvContacts.setAdapter(contactAdapter);
        Log.d("debug", "prefetch");
        fetchContacts();
        Log.d("debug", "postfetch");
        setupContactSelectedListener();
        Log.d("debug", "postSetup");
    }

    private void fetchContacts() {
        client = new ContactClient();
        client.getContacts(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject body) {
                JSONArray items = null;
                try {
                    Log.d("debug: ", "starting onSuccess: ");
                    items = body.getJSONArray("contacts");
                    Log.d("debug: ", "items: " + items);
                    ArrayList<Contact> contacts = Contact.fromJson(items);
                    for (Contact contact : contacts) {
                        contactAdapter.add(contact);
                        Log.d("debug: ", "" + contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                Log.d("debug: ", "error: " + e);
            }
        });
    }

    public void setupContactSelectedListener() {
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Launch the detail view passing movie as an extra
                Intent i = new Intent(ContactActivity.this, ContactDetailActivity.class);
                i.putExtra(CONTACT_KEY, contactAdapter.getItem(position));
                startActivity(i);
            }
        });
    }

}
