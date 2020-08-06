package com.jaivra.whatsreminder.gui.listener;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;

public interface MyItemView {
    public interface OnClickListener{
        public void onClick(ViewItemModel viewItemModel);
    }
}
