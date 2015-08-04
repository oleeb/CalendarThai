package com.oleeb.calendarthai.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.CalendarOfMonthDao;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.WeeksOfMonthDto;

import java.util.Calendar;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class MonthView {
    public void drawMonth(final Context context, final LinearLayout linearLayout_calendarthai, SharedPreferences sharedPrefs) {
        drawWeeks(context, linearLayout_calendarthai, sharedPrefs);
        Button bt_month_label = (Button)linearLayout_calendarthai.findViewById(R.id.month_label);
        ImageButton ibnt_prev_month_button = (ImageButton)linearLayout_calendarthai.findViewById(R.id.prev_month_button);
        ImageButton ibnt_next_month_button = (ImageButton)linearLayout_calendarthai.findViewById(R.id.next_month_button);
        bt_month_label.setVisibility(View.VISIBLE);
        ibnt_prev_month_button.setVisibility(View.VISIBLE);
        ibnt_next_month_button.setVisibility(View.VISIBLE);
        bt_month_label.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCalendar(context, linearLayout_calendarthai, CalendarThaiAction.ACTION_RESET_MONTH, null);
                }
            }
        );
        ibnt_prev_month_button.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCalendar(context, linearLayout_calendarthai, CalendarThaiAction.ACTION_PREVIOUS_MONTH, null);
                }
            }
        );
        ibnt_next_month_button.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawCalendar(context, linearLayout_calendarthai, CalendarThaiAction.ACTION_NEXT_MONTH, null);
                }
            }
        );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void drawWeeks(Context context, LinearLayout linearLayout_calendarthai, SharedPreferences sharedPrefs){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE)));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        CalendarOfMonthDao mCalendarOfMonthDao = new CalendarOfMonthDao(context);
        CalendarOfMonthDto mCalendarOfMonthDto = mCalendarOfMonthDao.getCalendar(cal);

        linearLayout_calendarthai.setBackgroundColor(0);

        //Set title Month Year
        TextView textView_month_label = (TextView)linearLayout_calendarthai.findViewById(R.id.month_label);
        textView_month_label.setText(mCalendarOfMonthDto.getMonthTitle() + " "
                + mCalendarOfMonthDto.getYearTitle());
        textView_month_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        LinearLayout linearLayout_calendar = (LinearLayout)linearLayout_calendarthai.findViewById(R.id.calendar);
        linearLayout_calendar.removeAllViews();
        linearLayout_calendar.setVisibility(View.VISIBLE);

        //Clear content All R.id.calendar
        linearLayout_calendar.removeAllViews();

        //Set Days Name Header title
        linearLayout_calendar.addView(DaysHeaderNameView
                .drawDaysHeaderName(context, mCalendarOfMonthDto.getDayNames()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        //Set Days
        WeeksOfMonthDto weeksOfMonthDto = mCalendarOfMonthDto.getWeeksOfMonthDto();
        for (int week = 0; week < weeksOfMonthDto.getMaximumWeeksOfMonth(); week++) {
            linearLayout_calendar.addView(WeekView.drawDays(context, linearLayout_calendarthai, weeksOfMonthDto.weeksOfMonth.get(week),
                    week, sharedPrefs), params);
        }
        //End set day
    }

    @SuppressLint("LongLogTag")
    public void drawCalendar(Context context, LinearLayout linearLayout_calendarthai, String action, Bundle extras) {
        Log.d("CalendarThaiWidget onReceive action", "" + action);
        Log.d("CalendarThaiWidget onReceive extras", "" + extras);
        SharedPreferences sharedPrefs = null;
        if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
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
        } else if(CalendarThaiAction.ACTION_LIST_DAY.equals(action)){
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        } else if (CalendarThaiAction.ACTION_DAY_DETAIL.equals(action)) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if(extras != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .commit();
            }
            WeekView.setDrawDayDetail(context, linearLayout_calendarthai, extras, sharedPrefs);
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
            drawMonth(context, linearLayout_calendarthai, sharedPrefs);
        }
    }
}