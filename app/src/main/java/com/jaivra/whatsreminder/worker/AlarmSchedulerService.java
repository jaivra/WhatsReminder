package com.jaivra.whatsreminder.worker;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.jaivra.whatsreminder.database.MyDbHelper;


public class AlarmSchedulerService extends LifecycleService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        MyDbHelper dbHelper = new MyDbHelper(getApplicationContext());
        dbHelper.getAllSingleReminders();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
