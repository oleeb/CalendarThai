package com.oleeb.calendarthai.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oleeb.calendarthai.CalendarThaiActivity;
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
    public static void drawWidgetMonth(Context context, LinearLayout linearLayout_container, SharedPreferences sharedPrefs) {
        drawWeeks(context, linearLayout_container, sharedPrefs);
        Button bt_month_label = (Button)linearLayout_container.findViewById(R.id.month_label);
        ImageButton ibt_prev_month_button = (ImageButton)linearLayout_container.findViewById(R.id.prev_month_button);
        ImageButton ibt_next_month_button = (ImageButton)linearLayout_container.findViewById(R.id.next_month_button);
        bt_month_label.setVisibility(View.VISIBLE);
        ibt_prev_month_button.setVisibility(View.VISIBLE);
        ibt_next_month_button.setVisibility(View.VISIBLE);
        bt_month_label.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CalendarThaiActivity calendarThaiActivity = new CalendarThaiActivity();
                        calendarThaiActivity.drawCalendar(CalendarThaiAction.ACTION_RESET_MONTH, null);
                    }
                }
        );
        ibt_prev_month_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CalendarThaiActivity calendarThaiActivity = new CalendarThaiActivity();
                        calendarThaiActivity.drawCalendar(CalendarThaiAction.ACTION_PREVIOUS_MONTH, null);
                    }
                }
        );
        ibt_next_month_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CalendarThaiActivity calendarThaiActivity = new CalendarThaiActivity();
                        calendarThaiActivity.drawCalendar(CalendarThaiAction.ACTION_NEXT_MONTH, null);
                    }
                }
        );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void drawWeeks(Context context, LinearLayout linearLayout_container, SharedPreferences sharedPrefs){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE)));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        CalendarOfMonthDao mCalendarOfMonthDao = new CalendarOfMonthDao(context);
        CalendarOfMonthDto mCalendarOfMonthDto = mCalendarOfMonthDao.getCalendar(cal);

        //Set title Month Year
        TextView textView_month_label = (TextView)linearLayout_container.findViewById(R.id.month_label);
        textView_month_label.setText(mCalendarOfMonthDto.getMonthTitle() + " "
                + mCalendarOfMonthDto.getYearTitle());
        textView_month_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        LinearLayout linearLayout_calendar = (LinearLayout)linearLayout_container.findViewById(R.id.calendar);
        linearLayout_calendar.setVisibility(View.VISIBLE);
        linearLayout_calendar.setBackgroundColor(sharedPrefs.getInt(CalendarThaiAction.BACKGROUND_COLOR, R.integer.COLOR_BACKGROUND_CALENDAR));

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
            linearLayout_calendar.addView(WeekView.drawDays(context, weeksOfMonthDto.weeksOfMonth.get(week),
                    week, sharedPrefs), params);
        }
        //End set day
    }
}