package com.contact.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.contact.R;
import com.contact.model.Contact;
import com.squareup.picasso.Picasso;

/**
 * Created by rtteal on 11/21/2014.
 */
public class ProfileActivity extends Activity {
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Fetch views
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        // Use the movie to populate the data into our views
        Contact contact = (Contact)
                getIntent().getSerializableExtra(ContactActivity.PROFILE_KEY);
        loadContact(contact);
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
}
