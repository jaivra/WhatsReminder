package com.jaivra.whatsreminder.gui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.database.MyDbHelper;
import com.jaivra.whatsreminder.gui.adapter.ReminderAdapter;
import com.jaivra.whatsreminder.gui.dialog.ReminderSettingsDialog;
import com.jaivra.whatsreminder.gui.listener.MyChangeListener;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.inc.gui.FragmentTab;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.model.GroupMessageDate;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SingleReminderTabFragment extends FragmentTab implements ReminderSettingsDialog.OnClickListener {
    private static String TAB_TITLE = "Single";

    protected MyChangeListener changeListener;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<ViewItemModel> items;

    protected MyDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_remider, container, false);

        this.dbHelper = new MyDbHelper(getContext());
        this.layoutManager = new LinearLayoutManager(getContext());
        this.items = new ArrayList<>();
        this.changeListener = (MyChangeListener) getActivity();

        formatItemsForView(getItems());

        this.recyclerView = layout.findViewById(R.id.recycler_view);
        this.adapter = new ReminderAdapter(items, getReminderView());

        MyItemView.OnClickListener<ProgrammedMessage> imageListener = programmedMessage -> {
            programmedMessage.setActive(!programmedMessage.isActive());
            adapter.notifyDataSetChanged();
        };

        this.adapter.setOnImageClickListener(imageListener);

        MyItemView.OnClickListener<ProgrammedMessage> settingsClickListener = programmedMessage -> {
            new ReminderSettingsDialog(programmedMessage, this).show(getFragmentManager(), "settingsDialog");
        };
        this.adapter.setOnSettingsClickListener(settingsClickListener);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

//        layout.findViewById(R.id.fab).setOnClickListener(view -> {
//            ContactDialog dialog = ContactDialog.newInstance(R.id.contact_name);
//            dialog.show(getFragmentManager(), "dialog)");
//        });
        return layout;
    }

    protected int getReminderView() {
        return R.layout.single_reminder_item;
    }

    protected List<ProgrammedMessage> getItems() {
        return dbHelper.getAllSingleReminders();
    }

    private void formatItemsForView(List<ProgrammedMessage> programmedMessages) {
        Date lastDate = null;
        Collections.sort(programmedMessages, (programmedMessage1, programmedMessage2) ->
                (int) (programmedMessage1.getDate().getTime() - programmedMessage2.getDate().getTime())
        );
        for (ProgrammedMessage programmedMessage : programmedMessages) {

            if (lastDate == null || programmedMessage.getDate().getTime() / (10 * 5) != lastDate.getTime() / (10 * 5)) {
                lastDate = programmedMessage.getDate();
                GroupMessageDate groupMessageDate = new GroupMessageDate(lastDate);
                items.add(groupMessageDate);
            }
            items.add(programmedMessage);
        }
    }

    @Override
    public String getTabTitle() {
        return TAB_TITLE;
    }

    @Override
    public void refresh() {
        items.clear();
        formatItemsForView(getItems());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDisableClick(ProgrammedMessage programmedMessage) {
        programmedMessage.setActive(false);
        dbHelper.updateReminder(programmedMessage);
        changeListener.onChange();
    }

    @Override
    public void onEnableClick(ProgrammedMessage programmedMessage) {
        programmedMessage.setActive(true);
        dbHelper.updateReminder(programmedMessage);
        changeListener.onChange();
    }

    @Override
    public void onDeleteClick(ProgrammedMessage programmedMessage) {
        programmedMessage.setType(ProgrammedMessage.TYPE_REMOVED);
        dbHelper.updateReminder(programmedMessage);
        changeListener.onChange();
    }

    public void onPeriodicClick(ProgrammedMessage programmedMessage) {
        programmedMessage.setType(ProgrammedMessage.TYPE_PERIODIC);
        dbHelper.updateReminder(programmedMessage);
        changeListener.onChange();
    }

    @Override
    public void onSingleClick(ProgrammedMessage programmedMessage) {

    }
}
