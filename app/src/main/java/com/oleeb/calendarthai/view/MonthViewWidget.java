package com.oleeb.calendarthai.view;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.CalendarOfMonthDao;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.WeeksOfMonthDto;

import java.util.Calendar;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class MonthViewWidget {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void drawWeeks(Context context, RemoteViews rv, SharedPreferences sharedPrefs, Class<?> cls){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE)));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        CalendarOfMonthDao mCalendarOfMonthDao = new CalendarOfMonthDao(context);
        CalendarOfMonthDto mCalendarOfMonthDto = mCalendarOfMonthDao.getCalendar(cal);
        rv.setViewVisibility(R.id.calendar, View.VISIBLE);
        rv.setInt(R.id.container_calendar, "setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.BACKGROUND_COLOR, R.integer.COLOR_BACKGROUND_CALENDAR));

        //Set title Month Year
        rv.setTextViewText(R.id.month_label, mCalendarOfMonthDto.getMonthTitle() + " "
                + mCalendarOfMonthDto.getYearTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rv.setTextViewTextSize(R.id.month_label, TypedValue.COMPLEX_UNIT_SP, 22);
        } else {
            rv.setFloat(R.id.month_label, "setTextSize", 22);
        }
        //Clear content All R.id.calendar
        rv.removeAllViews(R.id.calendar);

        //Set Days Name Header title
        rv.addView(R.id.calendar, DaysHeaderNameViewWidget
                .drawDaysHeaderName(context, mCalendarOfMonthDto.getDayNames()));
        //Set Days
        WeeksOfMonthDto weeksOfMonthDto = mCalendarOfMonthDto.getWeeksOfMonthDto();
        for (int week = 0; week < weeksOfMonthDto.getMaximumWeeksOfMonth(); week++) {
            rv.addView(R.id.calendar,
                    WeekViewWidget.drawDays(context, weeksOfMonthDto.weeksOfMonth.get(week),
                            week, sharedPrefs, cls));
        }
        //End set day
    }

    public static void drawWidgetMonth(Context context, RemoteViews rv, SharedPreferences sharedPrefs, Class<?> cls) {
        //rv.setInt(R.id.container_calendar, "setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.BACKGROUND_COLOR, R.integer.COLOR_BACKGROUND_CALENDAR));
        drawWeeks(context, rv, sharedPrefs, cls);

        rv.setViewVisibility(R.id.month_bar, View.VISIBLE);
        rv.setViewVisibility(R.id.prev_month_button, View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.prev_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, cls)
                                .setAction(CalendarThaiAction.ACTION_PREVIOUS_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setViewVisibility(R.id.next_month_button, View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.next_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, cls)
                                .setAction(CalendarThaiAction.ACTION_NEXT_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setOnClickPendingIntent(R.id.month_label,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, cls)
                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}