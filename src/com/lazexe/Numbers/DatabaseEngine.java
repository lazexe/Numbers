package com.lazexe.Numbers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.Html;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lazexe.Numbers.DigitDataBase.DATE;
import static com.lazexe.Numbers.DigitDataBase.DIGIT;
import static com.lazexe.Numbers.DigitDataBase.TABLE_NAME;

public class DatabaseEngine {

    private Context context;
    private DigitDataBase digitDataBase;

    public DatabaseEngine(Context context) {
        this.context = context;
        digitDataBase = new DigitDataBase(context);
    }

    public void saveToDatabaseDigit(String digit) {
        ContentValues contentValues = getContentValues(digit);
        SQLiteDatabase database = digitDataBase.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 9) {
            cursor.moveToFirst();
            database.delete(TABLE_NAME, DigitDataBase.UID + "=" + cursor.getString(0), null);
        }
        if (!digit.equals("") && !digit.equals(getPrevSavedValue())) {
            database.insert(TABLE_NAME, null, contentValues);
            setPrevValue(digit);
        }
        database.close();
        digitDataBase.close();
    }

    public ContentValues getContentValues(String digit) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        values.put(DATE, dateFormat.format(new Date()));
        values.put(DIGIT, digit);
        return values;
    }

    public String getResultMessage() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase database = digitDataBase.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.getCount() > 0) {
            int i = 1;
            cursor.moveToLast();
            while (true) {
                stringBuilder.append(i++).append(". ");
                stringBuilder.append(cursor.getString(1)).append(" -> ");
                stringBuilder.append("<b><font color=#FF0000>");
                stringBuilder.append(cursor.getString(2)).append("\n");
                stringBuilder.append("</b></font><br />");
                if (cursor.isFirst()) {
                    break;
                }
                cursor.moveToPrevious();
            }

        }
        database.close();
        digitDataBase.close();
        return stringBuilder.toString();
    }

    public String getPrevSavedValue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prevValue = prefs.getString("prev", "-1");
        return prevValue;
    }

    public void setPrevValue(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("prev", value).commit();
    }

    public void showDigitsDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(activity.getString(R.string.result));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(Html.fromHtml(getResultMessage()));
        dialogBuilder.setPositiveButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (activity instanceof ResultActivity)
                    activity.finish();
            }
        });
        dialogBuilder.create().show();
    }
}
