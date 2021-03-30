package com.jaivra.whatsreminder.gui.fragment;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.adapter.ReminderAdapter;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.List;

public class PeriodicReminderTabFragment extends SingleReminderTabFragment {
    private static String TAB_TITLE = "Periodic";


    protected List<ProgrammedMessage> getItems() {
        return dbHelper.getAllPeriodicReminders();
    }

    @Override
    protected int getReminderView() {
        return R.layout.periodic_reminder_item;
    }

    @Override
    public String getTabTitle() {
        return TAB_TITLE;
    }

    @Override
    public void onSingleClick(ProgrammedMessage programmedMessage) {
        programmedMessage.setType(ProgrammedMessage.TYPE_SINGLE);
        dbHelper.updateReminder(programmedMessage);
        changeListener.onChange();
    }
}
