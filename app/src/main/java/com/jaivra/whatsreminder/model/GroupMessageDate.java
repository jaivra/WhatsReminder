package com.jaivra.whatsreminder.model;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;

import java.util.Date;

public class GroupMessageDate implements ViewItemModel {
    Date date;

    public GroupMessageDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }
}
