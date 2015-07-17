package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dto.CalendarCurrent;
import com.oleeb.calendarthai.view.MonthView;
import com.oleeb.calendarthai.view.WeekView;

import java.util.Calendar;

/**
 * Created by Oleeb on 5/9/2015.
 */
@SuppressWarnings("ResourceType")
@SuppressLint("LongLogTag")
public class CalendarThaiWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("CalendarThaiWidget onUpdate", "" + context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences sharedPrefs = null;

        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        Log.d("CalendarThaiWidget onReceive action", "" + action);
        Log.d("CalendarThaiWidget onReceive extras", "" + extras);

        if(CalendarThaiAction.ACTION_APPWIDGET_ENABLED.equals(action)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings, true);
            }else{
                PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings_v8, true);
            }
        }else if (CalendarThaiAction.ACTION_APPWIDGET_UPDATE.equals(action)) {
            redrawWidgets(context,null);
        } else if (CalendarThaiAction.ACTION_SETTING.equals(action)){
            redrawWidgets(context,extras);
        } else if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            redrawWidgets(context,extras);
        } else if (CalendarThaiAction.ACTION_NEXT_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            redrawWidgets(context,extras);
        } else if (CalendarThaiAction.ACTION_RESET_MONTH.equals(action)) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .commit();
            }
            redrawWidgets(context,extras);
        } else if(CalendarThaiAction.ACTION_LIST_DAY.equals(action)){
            redrawWidgets(context,extras);
        } else if (CalendarThaiAction.ACTION_DAY_DETAIL.equals(action)) {
            redrawWidgets(context,extras);
        }
    }

    public void redrawWidgets(Context context, Bundle extras) {
        Log.d("CalendarThaiWidget redrawWidgets", "" + context);
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, CalendarThaiWidget.class));
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId, extras);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d("CalendarThaiWidget onAppWidgetOptionsChanged appWidgetId", "" + appWidgetId);
    }

    private void drawWidget(Context context, int appWidgetId, Bundle data) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Log.d("CalendarThaiWidget drawWidget appWidgetId", "" + appWidgetId);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);
        if(data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .commit();
            }
            rv = WeekView.drawWidgetDayDetail(context, rv, sharedPrefs, data, getClass());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                        .commit();
            }
            rv = MonthView.drawWidgetMonth(context, rv, sharedPrefs, getClass());
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
}
