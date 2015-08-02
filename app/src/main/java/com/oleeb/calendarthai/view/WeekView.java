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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.oleeb.calendarthai.CalendarThaiActivity;
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
    public static LinearLayout drawDays(Context context, DaysOfWeekDto daysOfWeekDto, int week, SharedPreferences sharedPrefs){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        LinearLayout linearLayout_row_week = (LinearLayout)
                inflater.inflate(R.layout.row_week, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        for(int i = 0; i < daysOfWeekDto.getMaximumDaysOfWeek(); i++){
            final Days days = daysOfWeekDto.daysOfWeek.get(i);

            RelativeLayout relativeLayout_cell_day = (RelativeLayout)
                    inflater.inflate(R.layout.cell_day, null);

            if (days.data.getBoolean(CalendarThaiAction.TO_MONTH)) { // in Month
//                relativeLayout_cell_day.setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                /* Some Code */
//                            }
//                        }
//
//                );
//                relativeLayout_cell_day.setOnClickPendingIntent(R.id.layRowDayContainer,
//                        PendingIntent.getBroadcast(context, Integer.parseInt(week + "" + i),
//                                new Intent(context, cls)
//                                        .setAction(CalendarThaiAction.ACTION_DAY_DETAIL)
//                                        .putExtras(days.data),
//                                PendingIntent.FLAG_UPDATE_CURRENT));
            }
            setDayDetail(context, relativeLayout_cell_day, days.data, sharedPrefs);
            linearLayout_row_week.addView(relativeLayout_cell_day, params);
        }
        return linearLayout_row_week;
    }

    @SuppressLint("NewApi")
    public static void setDayDetail(Context context, RelativeLayout relativeLayout_cell_day, Bundle data, SharedPreferences sharedPrefs) {
        showDays(context, relativeLayout_cell_day, data, sharedPrefs);
        showTxtHolidays(context, relativeLayout_cell_day, data, sharedPrefs);
        showWaxDays(context, relativeLayout_cell_day, data, sharedPrefs);
        showImgWanpra(context, relativeLayout_cell_day, data, sharedPrefs);
    }

    @SuppressLint("NewApi")
    public static void showDays(Context context, RelativeLayout relativeLayout_cell_day, Bundle data, SharedPreferences sharedPrefs) {
        TextView tvDay = (TextView)relativeLayout_cell_day.findViewById(R.id.tvDay);
        tvDay.setText(data.get(CalendarThaiAction.DAY).toString());
        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
            if(sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
                tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                tvDay.setPadding(0, -30, 0, -30);
            }

            if (data.getBoolean(CalendarThaiAction.TO_DAY)) {

                relativeLayout_cell_day.setBackgroundColor(sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.COLOR_TO_DAY_BACKGROUND));
                tvDay.setTextColor(sharedPrefs.getInt(CalendarThaiAction.TO_DAY_COLOR, R.integer.COLOR_TO_DAY));
            }else if (data.getInt(CalendarThaiAction.DAY_IN_WEEK) == 0
                        || data.get(CalendarThaiAction.HOLIDAY) != null) {
                tvDay.setTextColor(sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.COLOR_DAY_IN_HOLIDAY));
            }else{
                tvDay.setTextColor(sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.COLOR_DAY_IN_MONTH));
            }
        }else{
            tvDay.setTextColor(sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.COLOR_DAY_OTHER_MONTH));
        }
        tvDay.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    public static void showTxtHolidays(Context context, RelativeLayout relativeLayout_cell_day, Bundle data, SharedPreferences sharedPrefs) {
        TextView tvHoliday = (TextView) relativeLayout_cell_day.findViewById(R.id.tvHoliday);
        if (data.getBoolean(CalendarThaiAction.TO_MONTH) && data.get(CalendarThaiAction.HOLIDAY) != null) {
            tvHoliday.setText(data.get(CalendarThaiAction.HOLIDAY).toString());
            tvHoliday.setTextColor(sharedPrefs.getInt(CalendarThaiAction.TXT_HOLIDAY_COLOR, R.integer.COLOR_TXT_IN_HOLIDAY));

            if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
                tvHoliday.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                tvHoliday.setVisibility(View.VISIBLE);
            } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_HOLIDAY, false)){
                tvHoliday.setVisibility(View.VISIBLE);
            } else {
                tvHoliday.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void showWaxDays(Context context, RelativeLayout relativeLayout_cell_day, Bundle data, SharedPreferences sharedPrefs) {
        TextView tvWax = (TextView) relativeLayout_cell_day.findViewById(R.id.tvWax);
        String str_wax = (data.get(CalendarThaiAction.WAXING).equals("WX") ? "ขึ้น " : "แรม ")
                + data.get(CalendarThaiAction.WAXING_DAY) + " ค่ำ "
                + "เดือน " + data.get(CalendarThaiAction.WAXING_MONTH_2);

        tvWax.setText(str_wax);

        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
//            if (data.getBoolean(CalendarThaiAction.TO_DAY)) {
//                relativeLayout_cell_day.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.TO_DAY_COLOR, R.integer.COLOR_TO_DAY));
//            } else if (data.getInt(CalendarThaiAction.DAY_IN_WEEK) == 0
//                    || (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_COLOR_HOLIDAY, true)
//                    && data.get(CalendarThaiAction.HOLIDAY) != null)) {
//                relativeLayout_cell_day.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.COLOR_DAY_IN_HOLIDAY));
//            } else {
            tvWax.setTextColor(sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.COLOR_DAY_IN_MONTH));
//            }
        }else{
            tvWax.setTextColor(sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.COLOR_DAY_OTHER_MONTH));
        }

        if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)){
            tvWax.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            tvWax.setVisibility(View.VISIBLE);
        } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_WAX, false)){
            tvWax.setVisibility(View.VISIBLE);
        } else {
            tvWax.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    public static void showImgWanpra(Context context, RelativeLayout relativeLayout_cell_day, Bundle data, SharedPreferences sharedPrefs) {
        ImageView iv_wanpra = (ImageView) relativeLayout_cell_day.findViewById(R.id.iv_wanpra);
        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) {
            if (sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                    && data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                iv_wanpra.setImageResource(R.mipmap.ic_wanpra_xx);
                iv_wanpra.setPadding( 0, 0, 0, 0);
                iv_wanpra.setVisibility(View.VISIBLE);
            } else if (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_WANPRA, false)
                    && data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                iv_wanpra.setImageResource(R.mipmap.ic_wanpra);
                iv_wanpra.setVisibility(View.VISIBLE);
            } else {
                iv_wanpra.setVisibility(View.GONE);
            }
        }
    }

    public static RemoteViews drawWidgetDayDetail(Context context, RemoteViews rv, Bundle data, Class<?> cls, SharedPreferences sharedPrefs) {
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
        RemoteViews relativeLayout_cell_day = new RemoteViews(context.getPackageName(), R.layout.cell_day);
        //rowWeekRv.addView(R.id.row_week_container, WeekView.drawDayDetail(context, relativeLayout_cell_day, sharedPrefs, data));

        rv.addView(R.id.calendar, rowWeekRv);

        rv.setOnClickPendingIntent(R.id.calendar,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, cls)
                                .setAction(CalendarThaiAction.ACTION_LIST_DAY),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        return rv;
    }
}