package com.jaivra.whatsreminder.gui.dialog;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.adapter.ContactAdapter;
import com.jaivra.whatsreminder.inc.gui.FragmentTab;
import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.test.Generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class ReminderDialog extends DialogFragment {

    RecyclerView recyclerView;
    List<ContactMessage> contacts;
    ContactAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public static ReminderDialog newInstance(int title) {
        ReminderDialog frag = new ReminderDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View layout = inflater.inflate(R.layout.fragment_contact,
                container, false);
        this.recyclerView = layout.findViewById(R.id.contact_list);
        this.layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        this.contacts = new LinkedList<>();
        contacts.add(new Generator().generateContactMessage());
        System.out.println("COUNT * " + contacts.size());
        this.adapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(adapter);
        return layout;
    }



    private List<ContactMessage> getContacts() {
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        List<ContactMessage> contactsInfoList = new LinkedList<>();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {
            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactNumber = null;

                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);

                if (phoneCursor != null && phoneCursor.moveToNext()) {
                    contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneCursor.close();
                }

                ContactMessage contactsInfo = new ContactMessage.Builder()
                        .id(contactId)
                        .name(contactName)
                        .number(contactNumber)
                        .build();
                contactsInfoList.add(contactsInfo);
            }
        }
        cursor.close();
        return contactsInfoList;
    }


}
