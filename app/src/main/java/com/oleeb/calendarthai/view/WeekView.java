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
import android.widget.Button;
import android.widget.ImageButton;
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
    public static LinearLayout drawDays(final Context context, final LinearLayout linearLayout_calendarthai, DaysOfWeekDto daysOfWeekDto, int week, SharedPreferences sharedPrefs){
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
                relativeLayout_cell_day.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MonthView monthView = new MonthView();
                            monthView.drawCalendar(context, linearLayout_calendarthai, CalendarThaiAction.ACTION_DAY_DETAIL, days.data);
                        }
                    }
                );
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 120);
                }else{
                    tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                }
            }

            if (data.getBoolean(CalendarThaiAction.TO_DAY)) {
                if(!sharedPrefs.getBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)) {
                    relativeLayout_cell_day.setBackgroundColor(sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.COLOR_TO_DAY_BACKGROUND));
                }
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
                tvHoliday.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
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

    public static void setDrawDayDetail(final Context context, final LinearLayout linearLayout_calendarthai, Bundle data, SharedPreferences sharedPrefs) {
        LinearLayout.LayoutParams params = null;

        if(data.getBoolean(CalendarThaiAction.TO_DAY)) {
            LinearLayout linearLayout_container_calendar = (LinearLayout)linearLayout_calendarthai.findViewById(R.id.container_calendar);
            linearLayout_container_calendar.setBackgroundColor(sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.COLOR_TO_DAY_BACKGROUND));
        }

        ImageButton ibtn_prev_month_button = (ImageButton)linearLayout_calendarthai.findViewById(R.id.prev_month_button);
        ImageButton ibtn_next_month_button = (ImageButton)linearLayout_calendarthai.findViewById(R.id.next_month_button);

        Button ibtn_month_label = (Button)linearLayout_calendarthai.findViewById(R.id.month_label);
        ibtn_prev_month_button.setVisibility(View.GONE);
        ibtn_next_month_button.setVisibility(View.GONE);

        //Set title Month Year
        ibtn_month_label.setText(CalendarCurrent.MONTHNAMETH[data.getInt(CalendarThaiAction.MONTH)] + " "
                + CalendarCurrent.getYearTh(data.getInt(CalendarThaiAction.YEAR)));
        ibtn_month_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);

        LinearLayout linearLayout_calendar = (LinearLayout)linearLayout_calendarthai.findViewById(R.id.calendar);
        //Clear content All R.id.calendar
        linearLayout_calendar.removeAllViews();
        linearLayout_calendar.setVisibility(View.VISIBLE);

        //Set Days Name Header title
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout_row_header = (LinearLayout)
                inflater.inflate(R.layout.row_header, null);
        LinearLayout linearLayout_cell_header = (LinearLayout)
                inflater.inflate(R.layout.cell_header, null);

        TextView tvDayName = (TextView)linearLayout_cell_header.findViewById(R.id.tvDayName);
        tvDayName.setText(CalendarCurrent.DAYNAMESFULLTH[data.getInt(CalendarThaiAction.DAY_IN_WEEK)]);
        tvDayName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 4f);

        linearLayout_row_header.addView(linearLayout_cell_header);
        linearLayout_calendar.addView(linearLayout_row_header, params);

        LinearLayout linearLayout_row_week = (LinearLayout)
                inflater.inflate(R.layout.row_week, null);

        RelativeLayout relativeLayout_cell_day = (RelativeLayout)
                inflater.inflate(R.layout.cell_day, null);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        WeekView.setDayDetail(context, relativeLayout_cell_day, data, sharedPrefs);
        linearLayout_row_week.addView(relativeLayout_cell_day,params);

        linearLayout_calendar.addView(linearLayout_row_week, params);

        linearLayout_calendar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MonthView monthView = new MonthView();
                        monthView.drawCalendar(context, linearLayout_calendarthai, CalendarThaiAction.ACTION_LIST_DAY, null);
                    }
                }
        );
    }
}