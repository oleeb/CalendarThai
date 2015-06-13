package com.oleeb.calendarthai.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.CalendarThaiWidget;
import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dto.CalendarCurrent;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.dto.DaysOfWeekDto;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class WeekView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RemoteViews drawDays(Context context, DaysOfWeekDto daysOfWeekDto, int week, SharedPreferences sharedPrefs){
        RemoteViews rowWeekRv = new RemoteViews(context.getPackageName(), R.layout.row_week);
        for(int i = 0; i < daysOfWeekDto.getMaximumDaysOfWeek(); i++){
            final Days days = daysOfWeekDto.daysOfWeek.get(i);

            RemoteViews rowDayRv = new RemoteViews(context.getPackageName(), R.layout.cell_day);

            if (days.data.getBoolean(CalendarThaiAction.TO_MONTH)) { // in Month
                rowDayRv.setOnClickPendingIntent(R.id.layRowDayContainer,
                        PendingIntent.getBroadcast(context, Integer.parseInt(week + "" + i),
                                new Intent(context, CalendarThaiWidget.class)
                                        .setAction(CalendarThaiAction.ACTION_DAY_DETAIL)
                                        .putExtras(days.data),
                                PendingIntent.FLAG_UPDATE_CURRENT));
            }

            rowWeekRv.addView(R.id.row_week_container, WeekView.drawDayDetail(context, rowDayRv, sharedPrefs, days.data));
        }
        return rowWeekRv;
    }

    @SuppressLint("NewApi")
    public static RemoteViews drawDayDetail(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {
        rowDayRv = showDays(context, rowDayRv, sharedPrefs, data);
        rowDayRv = showTxtHolidays(context, rowDayRv, sharedPrefs, data);
        rowDayRv = showWaxDays(context, rowDayRv, sharedPrefs, data);
        rowDayRv = showImgWanpra(context, rowDayRv, sharedPrefs, data);
        return rowDayRv;
    }

    @SuppressLint("NewApi")
    public static RemoteViews showDays(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {
        rowDayRv.setTextViewText(R.id.tvDay, data.get(CalendarThaiAction.DAY).toString());
        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
            if(sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rowDayRv.setTextViewTextSize(R.id.tvDay, TypedValue.COMPLEX_UNIT_SP, 100);
                    rowDayRv.setViewPadding(R.id.tvDay, 0, -30, 0, -30);
                } else {
                    rowDayRv.setFloat(R.id.tvDay, "setTextSize", 100);
                }
            }

            if (data.getBoolean(CalendarThaiAction.TO_DAY)) {
                rowDayRv.setInt(R.id.layRowDayContainer,"setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.COLOR_TO_DAY_BACKGROUND));
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.TO_DAY_COLOR, R.integer.COLOR_TO_DAY));
            }else if (data.getInt(CalendarThaiAction.DAY_IN_WEEK) == 0
                        || data.get(CalendarThaiAction.HOLIDAY) != null) {
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.COLOR_DAY_IN_HOLIDAY));
            }else{
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.COLOR_DAY_IN_MONTH));
            }
        }else{
            rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.COLOR_DAY_OTHER_MONTH));
        }
        rowDayRv.setViewVisibility(R.id.tvDay, View.VISIBLE);
        return rowDayRv;
    }

    @SuppressLint("NewApi")
    public static RemoteViews showTxtHolidays(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {
        if (data.getBoolean(CalendarThaiAction.TO_MONTH) && data.get(CalendarThaiAction.HOLIDAY) != null) {
            rowDayRv.setTextViewText(R.id.tvHoliday, data.get(CalendarThaiAction.HOLIDAY).toString());
            rowDayRv.setTextColor(R.id.tvHoliday, sharedPrefs.getInt(CalendarThaiAction.TXT_HOLIDAY_COLOR, R.integer.COLOR_TXT_IN_HOLIDAY));

            if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rowDayRv.setTextViewTextSize(R.id.tvHoliday, TypedValue.COMPLEX_UNIT_SP, 22);
                } else {
                    rowDayRv.setFloat(R.id.tvHoliday, "setTextSize", 22);
                }
                rowDayRv.setViewVisibility(R.id.tvHoliday, View.VISIBLE);
            } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_HOLIDAY, false)){
                rowDayRv.setViewVisibility(R.id.tvHoliday, View.VISIBLE);
            } else {
                rowDayRv.setViewVisibility(R.id.tvHoliday, View.GONE);
            }
        }
        return rowDayRv;
    }

    @SuppressLint("NewApi")
    public static RemoteViews showWaxDays(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {
        String str_wax = (data.get(CalendarThaiAction.WAXING).equals("WX") ? "ขึ้น " : "แรม ")
                + data.get(CalendarThaiAction.WAXING_DAY) + " ค่ำ "
                + "เดือน " + data.get(CalendarThaiAction.WAXING_MONTH_2);

        rowDayRv.setTextViewText(R.id.tvWax, str_wax);

        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
//            if (data.getBoolean(CalendarThaiAction.TO_DAY)) {
//                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.TO_DAY_COLOR, R.integer.COLOR_TO_DAY));
//            } else if (data.getInt(CalendarThaiAction.DAY_IN_WEEK) == 0
//                    || (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_COLOR_HOLIDAY, true)
//                    && data.get(CalendarThaiAction.HOLIDAY) != null)) {
//                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.COLOR_DAY_IN_HOLIDAY));
//            } else {
                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.COLOR_DAY_IN_MONTH));
//            }
        }else{
            rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.COLOR_DAY_OTHER_MONTH));
        }

        if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rowDayRv.setTextViewTextSize(R.id.tvWax, TypedValue.COMPLEX_UNIT_SP, 22);
            } else {
                rowDayRv.setFloat(R.id.tvWax, "setTextSize", 22);
            }
            rowDayRv.setViewVisibility(R.id.tvWax, View.VISIBLE);
        } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_WAX, false)){
            rowDayRv.setViewVisibility(R.id.tvWax, View.VISIBLE);
        } else {
            rowDayRv.setViewVisibility(R.id.tvWax, View.GONE);
        }
        return rowDayRv;
    }

    @SuppressLint("NewApi")
    public static RemoteViews showImgWanpra(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {
        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
            if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                    && data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                rowDayRv.setImageViewResource(R.id.iv_wanpra, R.mipmap.ic_wanpra_xx);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rowDayRv.setViewPadding(R.id.iv_wanpra, 0, 30, 0, 0);
                }
                rowDayRv.setViewVisibility(R.id.iv_wanpra, View.VISIBLE);
            } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_WANPRA, false)
                    && data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                rowDayRv.setImageViewResource(R.id.iv_wanpra, R.mipmap.ic_wanpra);
                rowDayRv.setViewVisibility(R.id.iv_wanpra, View.VISIBLE);
            } else {
                rowDayRv.setViewVisibility(R.id.iv_wanpra, View.GONE);
            }
        }
        return rowDayRv;
    }

    public static RemoteViews drawWidgetDayDetail(Context context, RemoteViews rv, SharedPreferences sharedPrefs, Bundle data) {
        //data.get(CalendarThaiAction.TO_MONTH)
        rv.setViewVisibility(R.id.next_month_button, View.GONE);
        rv.setViewVisibility(R.id.prev_month_button, View.GONE);

        //Set title Month Year
        rv.setTextViewText(R.id.month_label, CalendarCurrent.MONTHNAMETH[data.getInt(CalendarThaiAction.MONTH)] + " "
                + CalendarCurrent.getYearTh(data.getInt(CalendarThaiAction.YEAR)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rv.setTextViewTextSize(R.id.month_label, TypedValue.COMPLEX_UNIT_SP, 28);
        } else {
            rv.setFloat(R.id.month_label, "setTextSize", 28);
        }

        rv.removeAllViews(R.id.calendar);

        if(data.getBoolean(CalendarThaiAction.TO_DAY)) {
            rv.setInt(R.id.container, "setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.COLOR_TO_DAY_BACKGROUND));
        }

        RemoteViews rowDayNamesRv = new RemoteViews(context.getPackageName(), R.layout.row_header);
        RemoteViews cellDayNamesRv = new RemoteViews(context.getPackageName(), R.layout.cell_header);
        cellDayNamesRv.setTextViewText(R.id.tvDayName, CalendarCurrent.DAYNAMESFULLTH[data.getInt(CalendarThaiAction.DAY_IN_WEEK)]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cellDayNamesRv.setTextViewTextSize(R.id.tvDayName, TypedValue.COMPLEX_UNIT_SP, 24);
        } else {
            cellDayNamesRv.setFloat(R.id.tvDayName, "setTextSize", 24);
        }
        rowDayNamesRv.addView(R.id.row_head_container, cellDayNamesRv);
        rv.addView(R.id.calendar, rowDayNamesRv);

        RemoteViews rowWeekRv = new RemoteViews(context.getPackageName(), R.layout.row_week);
        RemoteViews rowDayRv = new RemoteViews(context.getPackageName(), R.layout.cell_day);
        rowWeekRv.addView(R.id.row_week_container, WeekView.drawDayDetail(context, rowDayRv, sharedPrefs, data));

        rv.addView(R.id.calendar, rowWeekRv);

        rv.setOnClickPendingIntent(R.id.calendar,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalendarThaiWidget.class)
                                .setAction(CalendarThaiAction.ACTION_LIST_DAY),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        return rv;
    }
}