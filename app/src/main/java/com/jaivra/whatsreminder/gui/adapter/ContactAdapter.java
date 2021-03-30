package com.jaivra.whatsreminder.gui.adapter;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.inc.gui.MyViewHolder;
import com.jaivra.whatsreminder.model.ContactMessage;

import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {


    private List<ContactMessage> contacts;
    private MyItemView.OnClickListener contactClickListener;

    public ContactAdapter(List<ContactMessage> contacts) {
        this.contacts = contacts;
    }

    public void setOnContactClickListener(MyItemView.OnClickListener contactClickListener) {
        this.contactClickListener = contactClickListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        ContactHolder contactHolder = new ContactHolder(view, contactClickListener);
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

        MyItemView.OnClickListener clickListener;
        TextView namePreview;
        TextView name;
        TextView number;
        View container;
        final int[] BACKGROUND_COLOR = new int[]{R.color.activeGreen, R.color.colorAccent, R.color.purple, R.color.fuchsia, R.color.disableGray};


        public ContactHolder(@NonNull View itemView, MyItemView.OnClickListener clickListener) {
            super(itemView);

            this.clickListener = clickListener;
            this.namePreview = itemView.findViewById(R.id.name_preview);
            this.name = itemView.findViewById(R.id.name);
            this.number = itemView.findViewById(R.id.number);
            this.container = itemView.findViewById(R.id.container);
        }

        @Override
        public void bind(ViewItemModel viewItemModel) {
            ContactMessage contactMessage = (ContactMessage) viewItemModel;

            namePreview.setText(contactMessage.getNamePreview());
            name.setText(contactMessage.getName());
            number.setText(contactMessage.getNumber());

            ColorStateList color = ContextCompat.getColorStateList(namePreview.getContext(), BACKGROUND_COLOR[contactMessage.getName().length() % BACKGROUND_COLOR.length]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                namePreview.setBackgroundTintList(color);
            else
                ViewCompat.setBackgroundTintList(name, color);

            container.setOnClickListener(view -> clickListener.onClick(contactMessage));
        }
    }
}
