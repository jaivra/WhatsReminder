package com.jaivra.whatsreminder.gui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.adapter.ContactAdapter;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

public class ReminderSettingsDialog extends DialogFragment {

    private ProgrammedMessage programmedMessage;
    private OnClickListener listener;


    public ReminderSettingsDialog(ProgrammedMessage programmedMessage, OnClickListener listener) {
        this.programmedMessage = programmedMessage;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_settings_reminder,
                container, false);
        View disableButton = layout.findViewById(R.id.disable_button);
        View enableButton = layout.findViewById(R.id.enable_button);
        View deleteButton = layout.findViewById(R.id.delete_button);
        View periodicButton = layout.findViewById(R.id.periodic_button);
        View singleButton = layout.findViewById(R.id.single_button);

        disableButton.setOnClickListener(view -> {
            this.dismiss();
            listener.onDisableClick(programmedMessage);
        });
        enableButton.setOnClickListener(view -> {
            this.dismiss();
            listener.onEnableClick(programmedMessage);
        });

        deleteButton.setOnClickListener(view -> {
            this.dismiss();
            listener.onDeleteClick(programmedMessage);
        });

        periodicButton.setOnClickListener(view -> {
            this.dismiss();
            listener.onPeriodicClick(programmedMessage);
        });

        singleButton.setOnClickListener(view -> {
            this.dismiss();
            listener.onSingleClick(programmedMessage);
        });


        if (programmedMessage.isActive())
            enableButton.setVisibility(View.GONE);
        else
            disableButton.setVisibility(View.GONE);

        if (programmedMessage.getType() == ProgrammedMessage.TYPE_PERIODIC)
            periodicButton.setVisibility(View.GONE);
        else
            singleButton.setVisibility(View.GONE);


        return layout;
    }


    public interface OnClickListener {
        void onDisableClick(ProgrammedMessage programmedMessage);

        void onEnableClick(ProgrammedMessage programmedMessage);

        void onDeleteClick(ProgrammedMessage programmedMessage);

        void onPeriodicClick(ProgrammedMessage programmedMessage);

        void onSingleClick(ProgrammedMessage programmedMessage);
    }
}
