package com.contact.model;

import android.util.Log;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Models the data associated with a contact.
 * Created by rtteal on 11/16/2014.
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = -8959832007991513854L;
    private String fName, lName, phone, email, photo;

    public Contact(){}

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
        Log.d("fromJson", "creating contacts...");
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject contactObject = jsonArray.getJSONObject(i);
                Contact contact = Contact.fromJson(contactObject);
                if (contact != null) {
                    contacts.add(contact);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }

    public StringEntity getRequestParams(){
        // TODO figure out how to store userNmme
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userName", "taylor");
            jsonParams.put("fName", fName);
            jsonParams.put("lName", lName);
            jsonParams.put("image", photo);
            jsonParams.put("tel", phone);
            jsonParams.put("email", email);
            return new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
