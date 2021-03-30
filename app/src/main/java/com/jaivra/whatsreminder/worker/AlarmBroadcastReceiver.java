package com.jaivra.whatsreminder.worker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.jaivra.whatsreminder.database.MyDbHelper;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.Calendar;
import java.util.List;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public final static String ADD_ALARM_ACTION = "add_alarm_action";
    public final static String RECEIVE_SINGLE_ALARM = "receive_single_alarm_action";


//    intent.getAction is null for PendingActivity, discover why
    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
        if (intent.getAction() != null) {
            switch (intent.getAction()){
                case Intent.ACTION_BOOT_COMPLETED:
                    scheduleAlarms(context);
                    break;
                case RECEIVE_SINGLE_ALARM:
                    ProgrammedMessage programmedMessage = ProgrammedMessage.fromIntent(intent);
                    Toast.makeText(context, programmedMessage.getToContact().getName(), Toast.LENGTH_LONG).show();
                    startAlarmService(context, programmedMessage);
                    break;
            }
        }
        else{
            Toast.makeText(context, "NULL", Toast.LENGTH_LONG).show();
        }


    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        return false;
    }

    private void startAlarmService(Context context, ProgrammedMessage programmedMessage) {
        Intent intentService = new Intent(context, AlarmService.class);
        programmedMessage.fillIntent(intentService);
//        intentService.putExtra("dsadsadsa", intent.getStringExtra("dsadsadsa"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(Context context) {
//        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intentService);
//        } else {
//            context.startService(intentService);
//        }
    }

    private void scheduleAlarms(Context context) {
        MyDbHelper dbHelper = new MyDbHelper(context);
        List<ProgrammedMessage> messages = dbHelper.getAllSingleReminders();
        for (ProgrammedMessage message : messages) {
            message.schedule(context);
        }
    }
}