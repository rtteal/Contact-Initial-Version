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
 * Main activity for the Contact app.  This view shows the user's address book,
 * generates the <code>onClickListener</code> for when the user selects one of the
 * contacts.  It also produces the menu view with options for the user to view or
 *
 * Created by rtteal on 11/16/2014.
 */
public class ContactActivity extends Activity {
    private ListView lvContacts;
    private ContactAdapter contactAdapter;
    private Contact profile;
    public static final String CONTACT_KEY = "contact";
    public static final String PROFILE_KEY = "profile";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_profile:
                Intent i = new Intent(ContactActivity.this, ProfileActivity.class);
                i.putExtra(PROFILE_KEY, profile);
                startActivity(i);
                break;
            case R.id.edit_profile:
                i = new Intent(ContactActivity.this, EditProfileActivity.class);
                i.putExtra(PROFILE_KEY, profile);
                startActivity(i);
                break;
            case R.id.requests:
                i = new Intent(ContactActivity.this, RequestsActivity.class);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void fetchProfile(){
        ContactClient.get("profile/taylor", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject body) {
                profile = Contact.fromJson(body);
                Log.d("fetchProfile.onSuccess: ", "profile: " + profile);
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                Log.e("fetchProfile.onFailure: ", "error reaching endpoint", e);
            }

            @Override
            public void onFailure(Throwable e, JSONArray errorResponse) {
                super.onFailure(e, errorResponse);
                Log.e("fetchProfile.onFailure", "error reaching endpoint", e);
                // TODO generate failure message for user
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
        fetchContacts();
        setupContactSelectedListener();
        fetchProfile();
    }

    private void fetchContacts() {
        ContactClient.get("addressBook/taylor", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject body) {
                try {
                    JSONArray items = body.getJSONArray("contacts");
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
                Intent i = new Intent(ContactActivity.this, ContactDetailActivity.class);
                i.putExtra(CONTACT_KEY, contactAdapter.getItem(position));
                startActivity(i);
            }
        });
    }

}
