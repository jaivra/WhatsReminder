package com.jaivra.whatsreminder.gui.listener;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;

import java.util.List;

public interface MyItemView {
    public interface OnClickListener<T extends ViewItemModel>{
        public void onClick(T viewItemModel);
    }
}
