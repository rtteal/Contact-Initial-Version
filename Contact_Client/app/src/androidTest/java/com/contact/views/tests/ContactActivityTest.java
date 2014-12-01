package com.contact.views.tests;

import android.widget.ListView;

import com.contact.R;
import com.contact.views.ContactActivity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/**
 * Unit test for ContactActivity.
 * Created by rtteal on 11/26/2014.
 */
//@Config(manifest = "./app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ContactActivityTest {
    private ContactActivity activity;

    @Before
    public void setup()  {
        activity = Robolectric.buildActivity(ContactActivity.class).create().get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void contactSelected() throws Exception {
        ListView lvContacts = (ListView) activity.findViewById(R.id.lvContacts);
    }

}
