package com.oleeb.calendarthai;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class CalendarThaiWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("CalendarThaiWidget onUpdate", "" + context);
        for (int appWidgetId : appWidgetIds) {
            drawWidgetMonth(context, appWidgetId);
        }
    }

    public void redrawWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, CalendarThaiWidget.class));
        for (int appWidgetId : appWidgetIds) {
            drawWidgetMonth(context, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String action = intent.getAction();
        Log.d("CalendarThaiWidget onReceive action", "" + action);
//        context.getSharedPreferences(this,context.);
//        if(CalendarThaiAction.ACTION_APPWIDGET_ENABLED.equals(action)){
//            Intent intentCalendarThaiSettingsActivity = new Intent (context, CalendarThaiSettingsActivity.class);
//            intentCalendarThaiSettingsActivity.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intentCalendarThaiSettingsActivity);
//        }else
        if (CalendarThaiAction.ACTION_APPWIDGET_UPDATE.equals(action)){
             redrawWidgets(context);
             Intent intentCalendarThaiSettingsActivity = new Intent (context, CalendarThaiSettingsActivity.class);
             intentCalendarThaiSettingsActivity.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
             //context.startActivity(intentCalendarThaiSettingsActivity);
        } else if (CalendarThaiAction.ACTION_SETTING.equals(action)){
            redrawWidgets(context);
        } else if(CalendarThaiAction.ACTION_LIST_DAY.equals(action)){
             redrawWidgets(context);
        } else if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);
            sharedPrefs.edit()
                    .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                    .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (CalendarThaiAction.ACTION_NEXT_MONTH.equals(action)) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);
            sharedPrefs.edit()
                    .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                    .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (CalendarThaiAction.ACTION_RESET_MONTH.equals(action)) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPrefs.edit()
                    .remove(CalendarThaiAction.PREF_DATE)
                    .remove(CalendarThaiAction.PREF_MONTH)
                    .remove(CalendarThaiAction.PREF_YEAR).apply();
            redrawWidgets(context);
        } else if (CalendarThaiAction.ACTION_DAY_DETAIL.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                drawWidgetDayDetail(context, extras.getInt(CalendarThaiAction.APP_WIDGET_ID), extras);
            }
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d("CalendarThaiWidget onAppWidgetOptionsChanged appWidgetId", "" + appWidgetId);
        drawWidgetMonth(context, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private RemoteViews drawWidgetMonthBar(Context context, int appWidgetId, RemoteViews rv) {
        rv.setViewVisibility(R.id.month_bar, View.VISIBLE);
        rv.setViewVisibility(R.id.prev_month_button, View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.prev_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalendarThaiWidget.class)
                                .setAction(CalendarThaiAction.ACTION_PREVIOUS_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setViewVisibility(R.id.next_month_button, View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.next_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalendarThaiWidget.class)
                                .setAction(CalendarThaiAction.ACTION_NEXT_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setOnClickPendingIntent(R.id.month_label,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalendarThaiWidget.class)
                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        return rv;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawWidgetMonth(Context context, int appWidgetId) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Log.d("CalendarThaiWidget drawWidget appWidgetId", "" + appWidgetId);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);
        rv = drawWidgetMonthBar(context, appWidgetId, rv);
        rv = MonthView.drawWeeks(context, appWidgetId, rv, sharedPrefs);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawWidgetDayDetail(Context context, int appWidgetId, Bundle data) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Log.d("CalendarThaiWidget drawWidget appWidgetId", "" + appWidgetId);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);
        //data.get(CalendarThaiAction.TO_MONTH)
        rv.setViewVisibility(R.id.next_month_button, View.GONE);
        rv.setViewVisibility(R.id.prev_month_button, View.GONE);

        //Set title Month Year
        rv.setTextViewText(R.id.month_label, CalendarCurrent.MONTHNAMETH[data.getInt(CalendarThaiAction.MONTH)] + " "
                + CalendarCurrent.getYearTh(data.getInt(CalendarThaiAction.YEAR)));
        rv.setTextViewTextSize(R.id.month_label, TypedValue.COMPLEX_UNIT_SP, 28);

        if(data.getBoolean(CalendarThaiAction.TO_DAY)) {
            rv.setInt(R.id.container, "setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.TODAY_BACKGROUND));
        }else{
            rv.setInt(R.id.container, "setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.BACKGROUND_COLOR, R.integer.BACKGROUND_CALENDAR));
        }

        rv.removeAllViews(R.id.calendar);

        RemoteViews rowDayRv = new RemoteViews(context.getPackageName(), R.layout.row_day);
        //R.id.iv_wanpra
        rv.addView(R.id.calendar, WeekView.drawDayDetail(context, rowDayRv, sharedPrefs, data));
        rowDayRv.setTextViewTextSize(R.id.tvDay, TypedValue.COMPLEX_UNIT_SP, 100);
        rowDayRv.setTextViewTextSize(R.id.tvWax, TypedValue.COMPLEX_UNIT_SP, 24);
        rowDayRv.setTextViewTextSize(R.id.tvHoliday, TypedValue.COMPLEX_UNIT_SP, 28);
        //rowDayRv.sets
        //rowDayRv.setImageViewResource(R.id.iv_wanpra, R.drawable.iv_buddha);

        rv.setOnClickPendingIntent(R.id.calendar,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalendarThaiWidget.class)
                                .setAction(CalendarThaiAction.ACTION_LIST_DAY),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
}
