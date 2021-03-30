package com.jaivra.whatsreminder.gui.dialog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.adapter.ContactAdapter;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.test.Generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class ContactDialog extends DialogFragment {

    RecyclerView recyclerView;
    List<ContactMessage> contacts;
    ContactAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    MyItemView.OnClickListener clickListener;

    private ContactDialog(List<ContactMessage> contacts, MyItemView.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.contacts = contacts;
    }

    public static ContactDialog newInstance(List<ContactMessage> contacts, MyItemView.OnClickListener clickListener) {
        ContactDialog frag = new ContactDialog(contacts, clickListener);
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    public void setContacts(List<ContactMessage> contacts) {
        this.contacts = contacts;
        if (adapter != null)
            adapter.notifyDataSetChanged();
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

        this.adapter = new ContactAdapter(contacts);
        adapter.setOnContactClickListener(clickListener);

        recyclerView.setAdapter(adapter);
        return layout;
    }


}
