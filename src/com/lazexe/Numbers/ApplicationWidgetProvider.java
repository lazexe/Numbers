package com.lazexe.Numbers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class ApplicationWidgetProvider extends AppWidgetProvider {

    public static final String TAG = ApplicationWidgetProvider.class.getName();

    public static final String ACTION_ONE = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_ONE";
    public static final String ACTION_TWO = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_TWO";
    public static final String ACTION_THREE = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_THREE";
    public static final String ACTION_FOUR = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_FOUR";
    public static final String ACTION_FIVE = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_FIVE";
    public static final String ACTION_SIX = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_SIX";
    public static final String ACTION_SEVEN = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_SEVEN";
    public static final String ACTION_EIGHT = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_EIGHT";
    public static final String ACTION_NINE = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_NINE";
    public static final String ACTION_ZERO = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_ZERO";

    public static final String ACTION_SAVE = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_SAVE";
    public static final String ACTION_VIEW = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_VIEW";
    public static final String ACTION_CLEAR = "com.lazexe.Numbers.ApplicationWidgetProvider.ACTION_CLEAR";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.d(TAG, "onUpdate. Widget id: " + String.valueOf(appWidgetIds[i]));
            initButton(context, R.id.one_widget, ACTION_ONE, appWidgetIds[i]);
            initButton(context, R.id.two_widget, ACTION_TWO, appWidgetIds[i]);
            initButton(context, R.id.three_widget, ACTION_THREE, appWidgetIds[i]);
            initButton(context, R.id.four_widget, ACTION_FOUR, appWidgetIds[i]);
            initButton(context, R.id.five_widget, ACTION_FIVE, appWidgetIds[i]);
            initButton(context, R.id.six_widget, ACTION_SIX, appWidgetIds[i]);
            initButton(context, R.id.seven_widget, ACTION_SEVEN, appWidgetIds[i]);
            initButton(context, R.id.eight_widget, ACTION_EIGHT, appWidgetIds[i]);
            initButton(context, R.id.nine_widget, ACTION_NINE, appWidgetIds[i]);
            initButton(context, R.id.zero_widget, ACTION_ZERO, appWidgetIds[i]);
            initButton(context, R.id.clear_widget, ACTION_CLEAR, appWidgetIds[i]);
            initButton(context, R.id.save_widget, ACTION_SAVE, appWidgetIds[i]);
            initButton(context, R.id.view_widget, ACTION_VIEW, appWidgetIds[i]);
        }
    }

    private void initButton(Context context, int buttonId, String action, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent numberClick = new Intent(context, ApplicationWidgetProvider.class);
        numberClick.setAction(action);
        numberClick.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, numberClick, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(buttonId, pendingIntent);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String action = intent.getAction();
        String value = "";
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        if (action.equals(ACTION_ONE)) {
            value = "1";
        }
        if (action.equals(ACTION_TWO)) {
            value = "2";
        }
        if (action.equals(ACTION_THREE)) {
            value = "3";
        }
        if (action.equals(ACTION_FOUR)) {
            value = "4";
        }
        if (action.equals(ACTION_FIVE)) {
            value = "5";
        }
        if (action.equals(ACTION_SIX)) {
            value = "6";
        }
        if (action.equals(ACTION_SEVEN)) {
            value = "7";
        }
        if (action.equals(ACTION_EIGHT)) {
            value = "8";
        }
        if (action.equals(ACTION_NINE)) {
            value = "9";
        }
        if (action.equals(ACTION_ZERO)) {
            value = "0";
        }
        if (action.equals(ACTION_CLEAR)) {
            long currentClickTime = Calendar.getInstance().getTimeInMillis();
            long prevClickTime = getClearTime(context);
            long delay = currentClickTime - prevClickTime;
            if (delay < 200) {
                saveClearTime(context, currentClickTime);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().putString("widget_text", "").commit();
                setTextInWidget(context, widgetId, "");
            } else {
                saveClearTime(context, currentClickTime);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String text = preferences.getString("widget_text", "");
                text = NumbersActivity.removeLastSymbolFromString(text);
                preferences.edit().putString("widget_text", text).commit();
                setTextInWidget(context, widgetId, text);
            }
        }

        if (action.equals(ACTION_SAVE)) {
            Log.d(TAG, "ACTION_SAVE");
            DatabaseEngine dbEngine = new DatabaseEngine(context);
            dbEngine.saveToDatabaseDigit(getDigitFromWidgetPreference(context));
        }

        if (action.equals(ACTION_VIEW)) {
            Log.d(TAG, "ACTION_VIEW");
            Intent resultActivityIntent = new Intent(context, ResultActivity.class);
            resultActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(resultActivityIntent);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String text = preferences.getString("widget_text", "");
        text = text.concat(value);
        preferences.edit().putString("widget_text", text).commit();

        setTextInWidget(context, widgetId, text);
        super.onReceive(context, intent);
    }

    private void setTextInWidget(Context context, int widgetId, String text) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.input_textview_widget, text);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(widgetId, remoteViews);
    }

    private String getDigitFromWidgetPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String digit = preferences.getString("widget_text", "");
        return digit;
    }

    private void saveClearTime(Context context, long timeInMills) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong("clear_time", Calendar.getInstance().getTimeInMillis()).commit();
    }

    private long getClearTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong("clear_time", 1000);
    }
}
