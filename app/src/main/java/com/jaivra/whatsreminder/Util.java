package com.jaivra.whatsreminder;

import android.content.Intent;
import android.net.Uri;

import com.jaivra.whatsreminder.model.ProgrammedMessage;

public class Util {
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";


    public static Intent createSendMessageIntent(ProgrammedMessage programmedMessage){
        String url = "https://api.whatsapp.com/send?phone=" + programmedMessage.getToContact().getNumber() + "&text=" + programmedMessage.getBody();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }
}
