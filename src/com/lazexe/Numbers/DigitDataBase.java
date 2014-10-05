package com.lazexe.Numbers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DigitDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "digit_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "digits";

    public static final String UID = "_id";
    public static final String DATE = "date";
    public static final String DIGIT = "digit";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DATE + " VARCHAR(40),"
            + DIGIT + " VARCHAR(40));";

    public DigitDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
