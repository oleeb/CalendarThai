package com.oleeb.calendarthai.view;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.CalendarOfMonthDao;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.dto.DaysOfWeekDto;
import com.oleeb.calendarthai.dto.WeeksOfMonthDto;

import java.util.Calendar;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class MonthView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RemoteViews drawWeeks(Context context, int appWidgetId, RemoteViews rv, SharedPreferences sharedPrefs){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sp.getInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE)));
        cal.set(Calendar.MONTH, sp.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sp.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        CalendarOfMonthDao mCalendarOfMonthDao = new CalendarOfMonthDao(context);
        CalendarOfMonthDto mCalendarOfMonthDto = mCalendarOfMonthDao.getCalendar(cal);
        rv.setViewVisibility(R.id.calendar,View.VISIBLE);
        rv.setInt(R.id.container, "setBackgroundColor", sp.getInt(CalendarThaiAction.BACKGROUND_COLOR, R.integer.BACKGROUND_CALENDAR));

        //Set title Month Year
        rv.setTextViewText(R.id.month_label, mCalendarOfMonthDto.getMonthTitle() + " "
                + mCalendarOfMonthDto.getYearTitle());
        rv.setTextViewTextSize(R.id.month_label, TypedValue.COMPLEX_UNIT_SP, 22);
        //Clear content All R.id.calendar
        rv.removeAllViews(R.id.calendar);

        //Set Days Name Header title
        rv.addView(R.id.calendar, DaysHeaderNameView
                .drawDaysHeaderName(context, mCalendarOfMonthDto.getDayNames()));
        //Set Days
        WeeksOfMonthDto weeksOfMonthDto = mCalendarOfMonthDto.getWeeksOfMonthDto();
        for (int week = 0; week < weeksOfMonthDto.getMaximumWeeksOfMonth(); week++) {
            rv.addView(R.id.calendar,
                    WeekView.drawDays(context, appWidgetId, weeksOfMonthDto.weeksOfMonth.get(week),
                            week, sharedPrefs));
        }
        //End set day

        return rv;
    }
}