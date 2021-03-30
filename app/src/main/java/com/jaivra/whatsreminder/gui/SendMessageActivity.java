package com.jaivra.whatsreminder.gui;

import android.app.NotificationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.Util;
import com.jaivra.whatsreminder.model.ProgrammedMessage;
import com.ncorti.slidetoact.SlideToActView;

public class SendMessageActivity extends AppCompatActivity {

    private ProgrammedMessage programmedMessage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_activity2);

        programmedMessage = ProgrammedMessage.fromIntent(getIntent());
//
        SlideToActView slider = findViewById(R.id.slider);
        slider.setOnSlideCompleteListener(slideToActView -> sendMessageAction());


//        System.out.println("NOME : " + programmedMessage.getToContact().getName());
    }

    private void sendMessageAction() {
        final int notificationId = getIntent().getIntExtra(Util.NOTIFICATION_ID, -1);
        if (notificationId > 0) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(notificationId);
        }
        startActivity(Util.createSendMessageIntent(programmedMessage));

    }
}
