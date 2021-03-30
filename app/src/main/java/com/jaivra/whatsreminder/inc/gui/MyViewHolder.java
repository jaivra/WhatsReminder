package com.jaivra.whatsreminder.inc.gui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.gui.listener.MyItemView;

public abstract class MyViewHolder extends RecyclerView.ViewHolder {

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(ViewItemModel viewItemModel);
}
