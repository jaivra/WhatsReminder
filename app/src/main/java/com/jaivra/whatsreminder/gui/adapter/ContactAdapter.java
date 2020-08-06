package com.jaivra.whatsreminder.gui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.inc.gui.MyViewHolder;
import com.jaivra.whatsreminder.model.ContactMessage;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {


    private List<ContactMessage> contacts;

    public ContactAdapter(List<ContactMessage> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        ContactHolder contactHolder = new ContactHolder(view);
        return contactHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactHolder extends MyViewHolder {

        TextView namePreview;
        TextView name;
        TextView number;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            this.namePreview = itemView.findViewById(R.id.name_preview);
            this.name = itemView.findViewById(R.id.name);
            this.number = itemView.findViewById(R.id.number);
        }

        @Override
        public void bind(ViewItemModel viewItemModel) {
            ContactMessage contactMessage = (ContactMessage) viewItemModel;

            namePreview.setText(contactMessage.getNamePreview());
            name.setText(contactMessage.getName());
            number.setText(contactMessage.getNumber());
        }
    }
}
