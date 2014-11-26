package com.contact.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.contact.R;
import com.contact.client.ContactClient;
import com.contact.model.Contact;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * View for editing user's contact info.
 * Created by rtteal on 11/22/2014.
 */
public class EditProfileActivity extends Activity {
    private EditText etFName, etLName, etPhone, etEmail;
    private ImageView ivPhoto;
    private Button btSubmit;
    private Contact profile;
    private Contact contact;
    public static final String PROFILE_KEY = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btSubmit = (Button) findViewById(R.id.btSubmitProfile);
        createOnClickListener();
        contact = (Contact)
                getIntent().getSerializableExtra(ContactActivity.PROFILE_KEY);
        loadContact(contact);
    }

    // Populate the data for the contact
    public void loadContact(Contact contact) {
        // Populate data
        Picasso.with(this).load(contact.getPhoto()).
                placeholder(R.drawable.images).
                into(ivPhoto);
    }

    private void createOnClickListener() {
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                i.putExtra(PROFILE_KEY, profile);
                startActivity(i);
            }
        });
    }

    private void updateProfile(){
        profile = new Contact();
        profile.setfName(etFName.getText().toString());
        profile.setlName(etLName.getText().toString());
        profile.setPhone(etPhone.getText().toString());
        profile.setEmail(etEmail.getText().toString());
        profile.setPhoto(contact.getPhoto());
        ContactClient.post("updateProfile", profile.getRequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                Log.e("post", "update profile failed", e);
                // TODO generate failure message for user
            }

            @Override
            public void onFailure(Throwable e, JSONArray errorResponse) {
                super.onFailure(e, errorResponse);
                Log.e("post", "update profile failed", e);
                // TODO generate failure message for user
            }
        });
    }
}
