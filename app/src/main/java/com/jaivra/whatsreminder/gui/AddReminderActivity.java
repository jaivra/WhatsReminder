package com.jaivra.whatsreminder.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jaivra.whatsreminder.R;
import com.jaivra.whatsreminder.database.MyDbHelper;
import com.jaivra.whatsreminder.gui.dialog.ContactDialog;
import com.jaivra.whatsreminder.gui.dialog.DatePickerFactory;
import com.jaivra.whatsreminder.gui.listener.MyItemView;
import com.jaivra.whatsreminder.inc.gui.ViewItemModel;
import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddReminderActivity extends AppCompatActivity implements MyItemView.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    List<ContactMessage> contactMessages;

    View chooseContact;
    TextView chosenContact;

    TextView chosenDate;

    TextView chosenTime;
    CheckBox periodicCheckBox;

    ContactDialog contactDialog;

    EditText textMessage;

    ProgrammedMessage.Builder messageBuilder;

    int year = -1;
    int month = -1;
    int day = -1;
    int hour = -1;
    int minute = -1;

    MyDbHelper dbHelper;

    public static final String ADD_REMINDER_RESULT = "reminder_result";
    public static final int ADD_REMINDER = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder_activity);

        this.messageBuilder = new ProgrammedMessage.Builder();

        this.chooseContact = findViewById(R.id.choose_contact);
        this.chosenContact = findViewById(R.id.chosen_contact);

        this.chosenDate = findViewById(R.id.chosen_date);

        this.chosenTime = findViewById(R.id.chosen_time);

        this.textMessage = findViewById(R.id.text_message);

        this.periodicCheckBox = findViewById(R.id.checkbox_periodic);
        chosenDate.setOnClickListener(view -> {
            DatePickerDialog datePicker = DatePickerFactory.createDatePicker(this, this);
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePicker.show();
        });
        chosenTime.setOnClickListener(view ->
                DatePickerFactory.createTimePicker(this, this).show());

        chooseContact.setOnClickListener(view -> {
            if (contactDialog != null && !contactDialog.isVisible())
                contactDialog.show(getSupportFragmentManager(), "contact list");
        });

        new LoadContactTask(this).execute();

        findViewById(R.id.back_button).setOnClickListener(view -> finish());

        findViewById(R.id.add_reminder).setOnClickListener(this);

        dbHelper = new MyDbHelper(this);

        startAnimation();
    }

    private void startAnimation(){

        ColorStateList oldColors =  chosenTime.getTextColors(); //save original colors
        chosenTime.setTextColor(getResources().getColor(R.color.orangeWarning));
        ViewPropertyAnimator animator = chosenTime.animate().scaleX(1.5f).scaleY(1.5f).setDuration(300);
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                chosenTime.animate().scaleX(1).scaleY(1).setDuration(300).start();
                chosenTime.setTextColor(oldColors);
            }
        });
    }


    @Override
    public void onClick(ViewItemModel viewItemModel) {
        ContactMessage contactMessage = (ContactMessage) viewItemModel;
        messageBuilder.toContact(contactMessage);

        chosenContact.setText(contactMessage.getName());

        contactDialog.dismiss();
    }

    private void loadingContactsFinished(List<ContactMessage> contacts) {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        contactDialog = ContactDialog.newInstance(contacts, this);
        chooseContact.setClickable(true);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        String date = year + "-" + month + "-" + day;
        chosenDate.setText(date);
        setTime();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        Date date = new GregorianCalendar(0, 0, 0, hour, minute).getTime();
        DateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        chosenTime.setText(df.format(date));
        setTime();
    }

    private void setTime() {
        if (year != -1 && month != -1 && day != -1 && hour != -1 && minute != -1) {
            Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
            messageBuilder.date(date);
        }
    }

    @Override
    public void onClick(View view) {
        messageBuilder.body(textMessage.getText().toString());
        ProgrammedMessage programmedMessage = messageBuilder.build();
        if (programmedMessage.getBody() != null && !programmedMessage.getBody().isEmpty() &&
                programmedMessage.getDate() != null && programmedMessage.getToContact() != null) {
            programmedMessage.setActive(true);
            programmedMessage.setType(periodicCheckBox.isChecked() ? 2 : 1);

            dbHelper.insertReminder(programmedMessage);
            Intent intent = new Intent();
            intent.putExtra(ADD_REMINDER_RESULT, ADD_REMINDER);
            setResult(Activity.RESULT_OK, intent);

            programmedMessage.schedule(getApplicationContext());
            finish();
        } else
            Toast.makeText(this, "Insert all data", Toast.LENGTH_LONG).show();

    }

    private class LoadContactTask extends AsyncTask<Void, List<ContactMessage>, List<ContactMessage>> {

        Context context;

        LoadContactTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<ContactMessage> doInBackground(Void... voids) {
            return getContacts();
        }


        private List<ContactMessage> getContacts() {
            ContentResolver contentResolver = Objects.requireNonNull(context).getContentResolver();
            List<ContactMessage> contactsInfoList = new LinkedList<>();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactNumber = null;

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);

                    if (phoneCursor != null && phoneCursor.moveToNext()) {
                        contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneCursor.close();
                    }

                    ContactMessage contactsInfo = new ContactMessage.Builder()
                            .id(contactId)
                            .name(contactName)
                            .number(contactNumber)
                            .build();
                    contactsInfoList.add(contactsInfo);
                }
            }
            cursor.close();
            return contactsInfoList;
        }

        @Override
        protected void onPostExecute(List<ContactMessage> contacts) {
            super.onPostExecute(contactMessages);
            loadingContactsFinished(contacts);
        }
    }


    private static class MyAnimatorListener{
        View view;

        public MyAnimatorListener(View view) {
            this.view = view;
        }

        
    }

}
