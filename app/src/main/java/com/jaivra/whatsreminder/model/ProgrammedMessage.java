package com.jaivra.whatsreminder.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.worker.AlarmBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProgrammedMessage implements ViewItemModel {

    private long id;
    private ContactMessage toContact;
    private String body;
    private Date date;
    private int type;
    private boolean active;


    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_PERIODIC = 2;
    public static final int TYPE_PASSED = 3;
    public static final int TYPE_REMOVED = 4;

    private ProgrammedMessage(Builder builder) {
        id = builder.id;
        toContact = builder.toContact;
        body = builder.body;
        date = builder.date;
        type = builder.type;
        active = builder.active;
    }

    public long getId() {
        return id;
    }

    public ContactMessage getToContact() {
        return toContact;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setToContact(ContactMessage toContact) {
        this.toContact = toContact;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        String toastText = "Already exists PendingIntent : ";
        intent.setAction(AlarmBroadcastReceiver.RECEIVE_SINGLE_ALARM);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        fillIntent(intent);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) id, intent, 0);
        if (alarmPendingIntent != null || true) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
            }


            toastText = "One Time Alarm : ";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        toastText += sdf.format(date);
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

    }

    public void cancel() {

    }


    private final static String INTENT_ID = "message_id";
    private final static String INTENT_BODY = "message_body";
    private final static String INTENT_DATE = "message_date";
    private final static String INTENT_TYPE = "message_type";
    private final static String INTENT_ACTIVE = "message_active";

    public void fillIntent(Intent intent) {
        toContact.fillIntent(intent);

        intent.putExtra(INTENT_ID, id);
        intent.putExtra(INTENT_BODY, body);
        intent.putExtra(INTENT_DATE, date.getTime());
        intent.putExtra(INTENT_TYPE, type);
        intent.putExtra(INTENT_ACTIVE, active);
    }

    public static ProgrammedMessage fromIntent(Intent intent) {
        return new Builder()
                .id(intent.getLongExtra(INTENT_ID, -1))
                .body(intent.getStringExtra(INTENT_BODY))
                .toContact(ContactMessage.fromIntent(intent))
                .date(new Date(intent.getLongExtra(INTENT_DATE, -1)))
                .type(intent.getIntExtra(INTENT_TYPE, TYPE_SINGLE))
                .active(intent.getBooleanExtra(INTENT_ACTIVE, true))
                .build();
    }


    public static final class Builder {
        private long id;
        private ContactMessage toContact;
        private String body;
        private Date date;
        private int type;
        private boolean active;

        public Builder() {
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder toContact(ContactMessage toContact) {
            this.toContact = toContact;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public ProgrammedMessage build() {
            return new ProgrammedMessage(this);
        }
    }
}
