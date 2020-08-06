package com.jaivra.whatsreminder.test;

import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class Generator {


    private int messageCount;
    private int contactCount;
    private boolean periodic;

    public Generator() {
        this.messageCount = 0;
        this.contactCount = 0;
        this.periodic = false;
    }

    public ProgrammedMessage generateProgrammedMessage() {
        Date date = new Date(1596456344093L);
        boolean active;
        if (active = (new Random().nextInt(2)) > 0)
            date = new Date(1596458042481L);
        ProgrammedMessage result = new ProgrammedMessage.Builder()
                .body("Message " + messageCount)
                .periodic(periodic)
                .date(date)
                .toContact("Contact " + contactCount)
                .active(active)
                .build();
        return result;
    }

    public ContactMessage generateContactMessage() {
        return new ContactMessage.Builder()
                .id("2")
                .name("VALERIO")
                .number("333322323")
                .build();
    }

}
