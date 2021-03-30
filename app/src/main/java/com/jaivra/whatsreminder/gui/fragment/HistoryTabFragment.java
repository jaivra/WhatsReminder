package com.jaivra.whatsreminder.gui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.inc.gui.FragmentTab;

public class HistoryTabFragment extends FragmentTab {
    private static String TAB_TITLE = "History";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contact, container, false);
        return layout;
    }

    @Override
    public String getTabTitle() {
        return TAB_TITLE;
    }

    @Override
    public void refresh() {

    }
}
