package com.jaivra.whatsreminder.gui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.inc.gui.MyViewHolder;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.model.GroupMessageDate;
import com.jaivra.whatsreminder.model.ProgrammedMessage;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class SingleReminderAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<ViewItemModel> items;

    private final static int MESSAGE_TYPE = 1;
    private final static int DATE_TYPE = 2;

    private MyItemView.OnClickListener imageListener;

    public SingleReminderAdapter(List<ViewItemModel> messages) {
        this.items = messages;
    }

    public void setOnImageClickListener(MyItemView.OnClickListener imageListener) {
        this.imageListener = imageListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder;
        if (viewType == MESSAGE_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminder_item, parent, false);
            viewHolder = new SingleReminderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_date_item, parent, false);
            viewHolder = new DateHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(items.get(position));

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof ProgrammedMessage)
            return MESSAGE_TYPE;
        else
            return DATE_TYPE;
    }

    protected class SingleReminderHolder extends MyViewHolder {

        private TextView contactName;
        private TextView bodyMessage;
        private TextView dateMessage;
        private ImageView activeAlarmImage;
        private ImageView disableAlarmImage;
        private CardView cardView;


        public SingleReminderHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.card_view);
            this.contactName = itemView.findViewById(R.id.contact_name);
            this.bodyMessage = itemView.findViewById(R.id.body_message);
            this.dateMessage = itemView.findViewById(R.id.hour_message);
            this.activeAlarmImage = itemView.findViewById(R.id.active_alarm_image);
            this.disableAlarmImage = itemView.findViewById(R.id.disable_alarm_image);
        }

        public void bind(ViewItemModel viewItemModel) {
            ProgrammedMessage programmedMessage = (ProgrammedMessage) viewItemModel;
            contactName.setText(programmedMessage.getToContact());
            bodyMessage.setText(programmedMessage.getBody());

            DateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            this.dateMessage.setText(df.format(programmedMessage.getDate()));

            if (programmedMessage.isActive()) {
                disableAlarmImage.setVisibility(View.GONE);
                activeAlarmImage.setVisibility(View.VISIBLE);
                cardView.setBackgroundColor(Color.WHITE);
            } else {
                activeAlarmImage.setVisibility(View.GONE);
                disableAlarmImage.setVisibility(View.VISIBLE);
                cardView.setBackgroundColor(Color.rgb(224, 224, 224));
            }

            View.OnClickListener onClickListener = view -> imageListener.onClick(programmedMessage);
            activeAlarmImage.setOnClickListener(onClickListener);
            disableAlarmImage.setOnClickListener(onClickListener);


        }
    }

    protected static class DateHolder extends MyViewHolder {

        private TextView groupDate;

        public DateHolder(@NonNull View itemView) {
            super(itemView);
            this.groupDate = itemView.findViewById(R.id.group_date);
        }

        @Override
        public void bind(ViewItemModel viewItemModel) {
            GroupMessageDate groupMessageDate = (GroupMessageDate) viewItemModel;

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            this.groupDate.setText(df.format(groupMessageDate.getDate()));
        }
    }

}
