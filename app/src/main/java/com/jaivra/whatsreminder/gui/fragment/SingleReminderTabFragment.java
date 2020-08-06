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
import com.jaivra.whatsreminder.gui.adapter.SingleReminderAdapter;
import com.jaivra.whatsreminder.gui.dialog.ReminderDialog;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.inc.gui.FragmentTab;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.model.GroupMessageDate;
import com.jaivra.whatsreminder.model.ProgrammedMessage;
import com.jaivra.whatsreminder.test.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SingleReminderTabFragment extends FragmentTab implements MyItemView.OnClickListener {
    private static String TAB_TITLE = "Single";

    private RecyclerView recyclerView;
    private SingleReminderAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<ViewItemModel> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_remider, container, false);

        this.recyclerView = layout.findViewById(R.id.recycler_view);
        this.layoutManager = new LinearLayoutManager(getContext());
        this.items = new ArrayList<>();
        formatItemsForView(getItems());

        this.adapter = new SingleReminderAdapter(items);
        this.adapter.setOnImageClickListener(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        layout.findViewById(R.id.fab).setOnClickListener(view -> {
            ReminderDialog dialog = ReminderDialog.newInstance(R.id.contact_name);
            dialog.show(getFragmentManager(), "dialog)");
        });
        return layout;
    }

    private List<ProgrammedMessage> getItems() {
        Generator generator = new Generator();

        List<ProgrammedMessage> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add(generator.generateProgrammedMessage());
        }
        return items;
    }

    private void formatItemsForView(List<ProgrammedMessage> programmedMessages) {
        Date lastDate = null;
        Collections.sort(programmedMessages, (programmedMessage1, programmedMessage2) ->
                (int) (programmedMessage1.getDate().getTime() - programmedMessage2.getDate().getTime())
        );
        for (ProgrammedMessage programmedMessage : programmedMessages) {
            
            if (lastDate == null || programmedMessage.getDate().getTime() / (10*5) != lastDate.getTime() / (10*5)) {
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
    public void onClick(ViewItemModel viewItemModel) {
        ProgrammedMessage programmedMessage = (ProgrammedMessage) viewItemModel;
        programmedMessage.setActive(!programmedMessage.isActive());
        adapter.notifyDataSetChanged();
    }
}
