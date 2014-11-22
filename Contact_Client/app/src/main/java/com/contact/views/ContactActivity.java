package com.contact.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private Contact profile;
    public static final String CONTACT_KEY = "contact";
    public static final String PROFILE_KEY = "profile";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(ContactActivity.this, ProfileActivity.class);
                i.putExtra(PROFILE_KEY, profile);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void fetchProfile(){
        if (client == null)
            client = new ContactClient();
        client.getProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject body) {
                Log.d("fetchProfile.onSuccess: ", "starting onSuccess: " + body);
                profile = Contact.fromJson(body);
                Log.d("fetchProfile.onSuccess: ", "profile: " + profile);
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                Log.d("fetchProfile.onFailure: ", "error reaching endpoint", e);
            }
        });
    }

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
        Log.d("onCreate", "post setupContactSelectedListener");
        fetchProfile();
        Log.d("debug", "postSetup");
    }

    private void fetchContacts() {
        if (client == null)
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
                Log.d("onFailure: ", "error reaching endpoint", e);
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
