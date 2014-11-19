package com.contact.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rtteal on 11/16/2014.
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = -8959832007991513854L;
    private String fName, lName, phone, email, photo;

    private Contact(){}

    public static Contact fromJson(JSONObject jsonObject){
        Log.d("debug", "creating contact...");
        Contact c = new Contact();
        try {

            c.fName = jsonObject.getString("fName");
            c.lName = jsonObject.getString("lName");
            c.photo = jsonObject.getString("image");
            c.phone = jsonObject.getString("tel");
            c.email = jsonObject.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("debug", "" + c);
        return c;
    }

    public static ArrayList<Contact> fromJson(JSONArray jsonArray){
        Log.d("debug", "creating contacts...");
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject contactObject = null;
            try {
                contactObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Contact contact = Contact.fromJson(contactObject);
            if (contact != null) {
                contacts.add(contact);
            }
        }
        return contacts;
    }

    public String getName() {
        return fName + " " + lName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString(){
        return "[fName: " + fName
                + ", lName: " + lName
                + ", phone: " + phone
                + ", email: " + email
                + ", photo: " + photo
                + "]";
    }
}
