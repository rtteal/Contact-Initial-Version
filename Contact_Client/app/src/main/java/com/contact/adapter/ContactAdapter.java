package com.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contact.R;
import com.contact.model.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rtteal on 11/16/2014.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private final Context context;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_contact, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // Populate the data into the template view using the data object
        tvName.setText(contact.getName());
        Picasso.with(getContext()).load(contact.getPhoto()).into(ivPhoto);
        // Return the completed view to render on screen
        return convertView;
    }

}
