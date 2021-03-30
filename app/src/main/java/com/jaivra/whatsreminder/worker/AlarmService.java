package com.jaivra.whatsreminder.worker;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.Util;
import com.jaivra.whatsreminder.gui.MainActivity;
import com.jaivra.whatsreminder.gui.SendMessageActivity;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.concurrent.atomic.AtomicInteger;


public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL_ID";
    public static final String CHANNEL_NAME = "ALARM_SERVICE_CHANNEL_NAME";

    private final static AtomicInteger c = new AtomicInteger(0);


    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ProgrammedMessage programmedMessage = ProgrammedMessage.fromIntent(intent);
        Log.d("AlarmService", "true");

        String url = "https://api.whatsapp.com/send?phone=" + programmedMessage.getToContact().getNumber() + "&text=" + programmedMessage.getBody();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        PendingIntent sentPendingIntent = PendingIntent.getActivity(this, 0, i, 0);


        String alarmTitle = "INVIA MESSAGGIO";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(programmedMessage.getToContact().getName())
                .setContentText(programmedMessage.getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground, "CANCEL", pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "SENT", sentPendingIntent)
                .build();

//        mediaPlayer.start();

//        long[] pattern = { 0, 100, 1000 };
//        vibrator.vibrate(pattern, 0);

//        startForeground(1, notification);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        final int notificationId = c.incrementAndGet();
        notificationManager.notify(notificationId, notification);


        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if(!myKM.inKeyguardRestrictedInputMode()) {
            Intent dialogIntent = new Intent(this, SendMessageActivity.class);
            programmedMessage.fillIntent(dialogIntent);
            dialogIntent.putExtra(Util.NOTIFICATION_ID, notificationId);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

        }

// notificationId is a unique int for each notification that you must define
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
