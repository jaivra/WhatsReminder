package com.jaivra.whatsreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jaivra.whatsreminder.inc.gui.MyPagerAdapter;
import com.jaivra.whatsreminder.model.ContactMessage;
import com.jaivra.whatsreminder.model.ProgrammedMessage;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.jaivra.whatsreminder.model.ProgrammedMessage.TYPE_PASSED;
import static com.jaivra.whatsreminder.model.ProgrammedMessage.TYPE_PERIODIC;
import static com.jaivra.whatsreminder.model.ProgrammedMessage.TYPE_SINGLE;

public class MyDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Reminder.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "reminder";
    private static final String ID = "id";
    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_NUMBER = "contact_number";
    private static final String DATE = "date";
    private static final String MESSAGE = "message";
    private static final String TYPE = "type";
    private static final String STATE = "state";


    private static final int STATE_ACTIVE = 1;
    private static final int STATE_DISABLE = 0;


    private static final String SQL_CREATE_ENTRIES =
            "create table " + TABLE_NAME + "( " +
                    ID + " INTEGER primary key autoincrement, " +
                    CONTACT_NAME + " TEXT, " +
                    CONTACT_NUMBER + " TEXT, " +
                    DATE + " INTEGER, " +
                    MESSAGE + " TEXT, " +
                    TYPE + " INTEGER, " +
                    STATE + " INTEGER )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        System.out.println("************ " + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private ContentValues fillValues(ProgrammedMessage programmedMessage) {
        ContentValues values = new ContentValues();

        ContactMessage contactMessage = programmedMessage.getToContact();
        Date date = programmedMessage.getDate();

        values.put(CONTACT_NAME, contactMessage.getName());
        values.put(CONTACT_NUMBER, contactMessage.getNumber());
        values.put(DATE, date.getTime());
        values.put(MESSAGE, programmedMessage.getBody());
        values.put(TYPE, programmedMessage.getType());
        values.put(STATE, programmedMessage.isActive() ? 1 : 0);

        return values;
    }

    public void insertReminder(ProgrammedMessage programmedMessage) {
        ContentValues values = fillValues(programmedMessage);

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        System.out.println("INSERTION : " + id);
        programmedMessage.setId(id);
    }

    public void updateReminder(ProgrammedMessage programmedMessage) {
        ContentValues values = fillValues(programmedMessage);

        String selection = ID + " = ?";
        String[] selectionArgs = {String.valueOf(programmedMessage.getId())};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public List<ProgrammedMessage> getAllSingleReminders() {
        String selection = TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(TYPE_SINGLE)};
        return getReminders(selection, selectionArgs);
    }

    public List<ProgrammedMessage> getAllPeriodicReminders() {
        String selection = TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(TYPE_PERIODIC)};
        return getReminders(selection, selectionArgs);
    }

    public List<ProgrammedMessage> getAllHistoricReminders() {
        String selection = TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(TYPE_PASSED)};
        return getReminders(selection, selectionArgs);
    }

    public List<ProgrammedMessage> getAllActiveReminders() {
        String selection = STATE + " = ?";
        String[] selectionArgs = {String.valueOf(STATE_ACTIVE)};
        return getReminders(selection, selectionArgs);
    }


    private List<ProgrammedMessage> getReminders(String selection, String[] selectionArgs) {
        String sortOrder = DATE;

        Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<ProgrammedMessage> result = new LinkedList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NAME));
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_NUMBER));
            String message = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE));
            int state = cursor.getInt(cursor.getColumnIndexOrThrow(STATE));

            ContactMessage contactMessage = new ContactMessage.Builder()
                    .name(contactName)
                    .number(contactNumber)
                    .build();
            ProgrammedMessage programmedMessage = new ProgrammedMessage.Builder()
                    .id(id)
                    .toContact(contactMessage)
                    .body(message)
                    .date(new Date(date))
                    .type(type)
                    .active(state == 1)
                    .build();
            result.add(programmedMessage);
        }
        cursor.close();
        return result;
    }
}
