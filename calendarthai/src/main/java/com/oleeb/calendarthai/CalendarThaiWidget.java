package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.CalendarOfMonthDao;
import com.oleeb.calendarthai.dao.DataOfDayDao;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.CalendarUtils;
import com.oleeb.calendarthai.dto.DataOfDayDto;

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
        Log.d(CalendarThaiWidget.class.getName(), "onUpdate appWidgetIds:" + (appWidgetIds != null ? appWidgetIds.length : 0));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.d("onReceive action", "" + action);
        Bundle extras = null;
        int appWidgetId = 0;
        if (action != null) {
            extras = intent.getExtras();
            Log.d("onReceive extras", "" + extras);
            if(extras != null) {
                appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
        }

        if(AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)){
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings, true);
        }else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            redrawWidgets(context);
        } else if (CalendarThaiAction.ACTION_SETTING.equals(action)){
            redrawWidgets(context);
        } else if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.MONTH, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListDay(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_NEXT_MONTH.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.MONTH, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListDay(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_RESET_MONTH.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId)
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId)
                        .commit();
            }
            drawWidgetListDay(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_LIST_DAY.equals(action)) {
            if(extras != null && extras.containsKey(CalendarThaiAction.EXTRA_MONTH_DETAIL)) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

                int month = extras.getInt(CalendarThaiAction.EXTRA_MONTH_DETAIL);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                            .apply();
                } else {
                    sharedPrefs.edit()
                            .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                            .commit();
                }
            }
            drawWidgetListDay(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_DETAIL_DAY.equals(action) && extras != null) {
            drawWidgetDetailDay(context, appWidgetId, extras);
        } else if (CalendarThaiAction.ACTION_LIST_MONTH.equals(action)) {
            Log.d("ACTION_LIST_MONTH extras",""+(extras != null && extras.containsKey(CalendarThaiAction.EXTRA_YEAR_DETAIL)));
            if(extras != null && extras.containsKey(CalendarThaiAction.EXTRA_YEAR_DETAIL)) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

                int year = extras.getInt(CalendarThaiAction.EXTRA_YEAR_DETAIL);
                Log.d("ACTION_LIST_MONTH year",""+year);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
                cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
                cal.set(Calendar.YEAR, year);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                            .apply();
                } else {
                    sharedPrefs.edit()
                            .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                            .commit();
                }
            }
            drawWidgetListMonth(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_PREVIOUS_YEAR.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListMonth(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_NEXT_YEAR.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListMonth(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_LIST_YEAR.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            drawWidgetListYear(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_PREVIOUS_YEAR_GROUP.equals(action)
                && extras != null && extras.containsKey(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

            int numOfYear = extras.getInt(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP, 20);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, -numOfYear);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListYear(context, appWidgetId);
        } else if (CalendarThaiAction.ACTION_NEXT_YEAR_GROUP.equals(action)
                && extras != null && extras.containsKey(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

            int numOfYear = extras.getInt(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP, 20);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, numOfYear);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListYear(context, appWidgetId);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId)
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId)
                        .remove(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId)
                        .commit();
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {

        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d("onAppWidgetOptionsChanged appWidgetId", "" + appWidgetId);
    }

    public void redrawWidgets(Context context) {
        //Log.d("CalendarThaiWidget redrawWidgets", "" + context);
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, this.getClass()));
        for (int appWidgetId : appWidgetIds) {
            drawWidgetListDay(context, appWidgetId);
        }
    }

    private void drawWidgetListDay(Context context, int appWidgetId) {
        Log.d("drawWidgetListDay appWidgetId", "" + appWidgetId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));

        CalendarOfMonthDao calendarOfMonthDao = new CalendarOfMonthDao(cal);
        CalendarOfMonthDto calendarOfMonthDto = calendarOfMonthDao.getCalendar();
        RemoteViews rv_activity_calendar_thai;
        rv_activity_calendar_thai = new RemoteViews(context.getPackageName(), R.layout.activity_calendar_thai);
        rv_activity_calendar_thai.removeAllViews(R.id.llCalendarContainer);
        //set calendar background color
//        rv_activity_calendar_thai.setInt(R.id.llCalendarContainer, "setBackgroundResource", R.drawable.shape_rounded_corners);
        rv_activity_calendar_thai.setInt(R.id.llCalendarContainer, "setBackgroundColor",
                sharedPrefs.getInt(context.getString(R.string.key_background_widget),
                        context.getResources().getInteger(R.integer.COLOR_BACKGROUND_CALENDAR)));

        //month bar
        RemoteViews rv_row_month_bar = new RemoteViews(context.getPackageName(), R.layout.row_month_bar);
        rv_row_month_bar.setTextViewText(R.id.tvTitleMonth, calendarOfMonthDto.getMonthTitle() + " " + calendarOfMonthDto.getYearTitle());
        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_month_bar);

        //days header name
        RemoteViews rv_row_days_header = new RemoteViews(context.getPackageName(), R.layout.row_days_header);
        for(int i = 0; i < CalendarOfMonthDto.DAYS_PER_WEEK; i++){
            RemoteViews rv_cell_header = new RemoteViews(context.getPackageName(), R.layout.cell_days_header);
            rv_cell_header.setTextViewText(R.id.tvDayName, CalendarUtils.DAYNAMESTH[i]);
            rv_row_days_header.addView(R.id.llRowDaysHeader, rv_cell_header);
        }
        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_days_header);

        //Days of Month
        RemoteViews rv_row_calendar = new RemoteViews(context.getPackageName(), R.layout.row_calendar);

        int thisMonth = calendarOfMonthDto.getMonth();
        int thisYear = calendarOfMonthDto.getYear();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int numWeeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        cal.add(Calendar.DAY_OF_MONTH, 1 - todayDayOfWeek);

        DataOfDayDao dataOfDayDao;
        DataOfDayDto dataOfDayDto;
        for (int week = 0; week < numWeeks; week++) {
            RemoteViews rv_row_week = new RemoteViews(context.getPackageName(), R.layout.row_week);
            for (int day = 0; day < CalendarOfMonthDto.DAYS_PER_WEEK; day++) {
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean isToday = (cal.get(Calendar.YEAR) == CalendarUtils.getCurrentYear())
                        && (cal.get(Calendar.MONTH) == CalendarUtils.getCurrentMonth())
                        && (cal.get(Calendar.DAY_OF_YEAR) == CalendarUtils.getCurrentDay());

                dataOfDayDao = new DataOfDayDao(context, cal);
                dataOfDayDto = dataOfDayDao.getData();
                dataOfDayDto.getData().putBoolean(CalendarThaiAction.TO_DAY, isToday);
                //set views text days
                RemoteViews rv_cell_days = new RemoteViews(context.getPackageName(), R.layout.cell_days);
                rv_cell_days.setTextViewText(R.id.tvDay, String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                //set text color
                if (!inMonth) {// day other month
                    rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_other_month));
                } else if (day == 0){// in holiday
                    rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_holiday));
                } else {// in month
                    rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_month));
                }
                //sert To day Background
                if (isToday) {// to day
                    rv_cell_days.setInt(R.id.layCellDayContainer, "setBackgroundColor", context.getResources().getInteger(R.color.today_background));
//                    rv_cell_days.setInt(R.id.layCellDayContainer, "setBackgroundResource", R.drawable.shape_circle);
                }
                //set views text holidays and important day
                if(dataOfDayDto.getData() != null  && dataOfDayDto.getData().getString(CalendarThaiAction.IMPORTANT_DESC) != null && !dataOfDayDto.getData().getString(CalendarThaiAction.IMPORTANT_DESC).equals("")) {
                    rv_cell_days.setTextViewText(R.id.tvHoliday, dataOfDayDto.getData().getString(CalendarThaiAction.IMPORTANT_DESC));
                    rv_cell_days.setViewVisibility(R.id.tvHoliday, View.VISIBLE);
                    //set text color
                    if (inMonth) {
                        if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                            rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_holiday));
                            rv_cell_days.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_holiday));
                        } else if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                            rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_important));
                            rv_cell_days.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_important));
                        }
                    } else {
                        if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                            rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_holiday_other_month));
                            rv_cell_days.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_holiday_other_month));
                        } else if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                            rv_cell_days.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_important_other_month));
                            rv_cell_days.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_important_other_month));
                        }
                    }
                }
                //set views text waxing waning
                if(dataOfDayDto.getData() != null && !dataOfDayDto.getData().getString(CalendarThaiAction.WAXING_DAY).equals("")) {
                    String str_wax = (dataOfDayDto.getData().getString(CalendarThaiAction.WAXING).equals(CalendarThaiAction.WAXING) ? context.getResources().getString(R.string.txt_waxing) : context.getResources().getString(R.string.txt_waning))
                            + " " + dataOfDayDto.getData().getString(CalendarThaiAction.WAXING_DAY) + " " + context.getResources().getString(R.string.txt_subfix)
                            + " " + context.getResources().getString(R.string.txt_month) +" "+ dataOfDayDto.getData().getString(CalendarThaiAction.WAXING_MONTH_2);
                    rv_cell_days.setTextViewText(R.id.tvWax, str_wax);
                    //set text color
                    if (!inMonth) {// day other month
                        rv_cell_days.setTextColor(R.id.tvWax, context.getResources().getInteger(R.color.day_other_month));
                    } else {
                        rv_cell_days.setTextColor(R.id.tvWax, context.getResources().getInteger(R.color.day_in_month));
                    }
                } else {
                    rv_cell_days.setViewVisibility(R.id.tvWax, View.VISIBLE);
                }

                //set image wanpra
                if (inMonth && dataOfDayDto.getData().getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                    rv_cell_days.setImageViewResource(R.id.ivWanpra, R.drawable.ic_wanpra);
                    rv_cell_days.setViewVisibility(R.id.ivWanpra, View.VISIBLE);
                }else if(dataOfDayDto.getData().getBoolean(CalendarThaiAction.WAXING_WANPRA)){
                    rv_cell_days.setImageViewResource(R.id.ivWanpra, R.drawable.ic_wanpra_other);
                    rv_cell_days.setViewVisibility(R.id.ivWanpra, View.VISIBLE);
                }
                //btn control
                if (inMonth) {
                    rv_cell_days.setOnClickPendingIntent(R.id.layCellDayContainer,
                            PendingIntent.getBroadcast(context, Integer.parseInt(appWidgetId + "" + cal.get(Calendar.YEAR) + "" + cal.get(Calendar.DAY_OF_YEAR)),
                                    new Intent(context, getClass())
                                            .setAction(CalendarThaiAction.ACTION_DETAIL_DAY)
                                            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                            .putExtra(CalendarThaiAction.EXTRA_DAY_DETAIL, dataOfDayDto.getData()),
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }

                rv_row_week.addView(R.id.linLayRowWeekContainer, rv_cell_days);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }// End Day
            rv_row_calendar.addView(R.id.llRowCalendarContainer, rv_row_week);
        }// End Week

        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_calendar);

        //btn control
        if(cal.get(Calendar.YEAR) > context.getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvPrevMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_PREVIOUS_MONTH)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvPrevMonth, View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < context.getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvNextMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_NEXT_MONTH)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvNextMonth, View.INVISIBLE);
        }
        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvCurrMonth,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvTitleMonth,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_LIST_MONTH)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, rv_activity_calendar_thai);
    }

    private void drawWidgetDetailDay(Context context, final int appWidgetId, Bundle extras) {
        Log.d("drawWidgetDetailDay appWidgetId", "" + appWidgetId);
        Bundle data = extras.getBundle(CalendarThaiAction.EXTRA_DAY_DETAIL);
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        RemoteViews rv_activity_calendar_thai;
        rv_activity_calendar_thai = new RemoteViews(context.getPackageName(), R.layout.activity_calendar_thai);
        rv_activity_calendar_thai.removeAllViews(R.id.llCalendarContainer);

        RemoteViews rv_cell_day_detail = new RemoteViews(context.getPackageName(), R.layout.cell_day_detail);

        if(data != null) {
            //day
            rv_cell_day_detail.setTextViewText(R.id.tvDay, String.valueOf(data.getInt(CalendarThaiAction.DAY)));
            if(data.getBoolean(CalendarThaiAction.TO_DAY)) {
                rv_cell_day_detail.setInt(R.id.llDayContainer, "setBackgroundColor", context.getResources().getInteger(R.color.today_background));
            }
            if (data.getInt(CalendarThaiAction.DAY_OF_WEEK) == 1 || data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                rv_cell_day_detail.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_holiday));
//                rv_cell_day_detail.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_holiday));
            } else if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                rv_cell_day_detail.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_important));
//                rv_cell_day_detail.setTextColor(R.id.tvHoliday, context.getResources().getInteger(R.color.day_in_important));
            } else {
                rv_cell_day_detail.setTextColor(R.id.tvDay, context.getResources().getInteger(R.color.day_in_month));
            }
            // wax
            if (data != null && !data.getString(CalendarThaiAction.WAXING_DAY).equals("")) {
                String str_wax = (data.getString(CalendarThaiAction.WAXING).equals(CalendarThaiAction.WAXING) ? context.getResources().getString(R.string.txt_waxing) : context.getResources().getString(R.string.txt_waning))
                        + " " + data.getString(CalendarThaiAction.WAXING_DAY) + " " + context.getResources().getString(R.string.txt_subfix)
                        + " " + context.getResources().getString(R.string.txt_month) + " " + data.getString(CalendarThaiAction.WAXING_MONTH_2);
                rv_cell_day_detail.setTextViewText(R.id.tvWax, str_wax);
                rv_cell_day_detail.setTextColor(R.id.tvWax, context.getResources().getInteger(R.color.day_in_month));
            } else {
                rv_cell_day_detail.setViewVisibility(R.id.tvWax, View.VISIBLE);
            }
            //month year
            rv_cell_day_detail.setTextViewText(R.id.tvTitleMonth, CalendarUtils.MONTHNAMETH[data.getInt(CalendarThaiAction.MONTH)]+" "+CalendarUtils.getYearTh(data.getInt(CalendarThaiAction.YEAR)));
            rv_cell_day_detail.setTextColor(R.id.tvTitleMonth, context.getResources().getInteger(R.color.day_in_month));
            //day name
            rv_cell_day_detail.setTextViewText(R.id.tvDayName, CalendarUtils.DAYNAMESFULLTH[data.getInt(CalendarThaiAction.DAY_OF_WEEK) - 1]);
            rv_cell_day_detail.setTextColor(R.id.tvDayName, context.getResources().getInteger(R.color.day_in_month));
            //image wanpra
            if (data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                rv_cell_day_detail.setImageViewResource(R.id.ivWanpra, R.drawable.ic_wanpra_xx);
            } else {
                rv_cell_day_detail.setViewVisibility(R.id.ivWanpra, View.VISIBLE);
            }
        }

        rv_cell_day_detail.setOnClickPendingIntent(R.id.llDetailDayContainer,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_LIST_DAY)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        //set calendar background color
        rv_cell_day_detail.setInt(R.id.llDetailDayContainer, "setBackgroundColor",
                sharedPrefs.getInt(context.getString(R.string.key_background_widget),
                        context.getResources().getInteger(R.integer.COLOR_BACKGROUND_CALENDAR)));

        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_cell_day_detail);

        //Event View
        int maxView = 6;
        RemoteViews rv_row_event = new RemoteViews(context.getPackageName(), R.layout.row_event);
        RemoteViews rv_cell_event =  null;

        //Holiday
        if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
            rv_cell_event = new RemoteViews(context.getPackageName(),
                    R.layout.cell_event);
//            rv_cell_event.setImageViewResource(R.id.imageView1, R.id.ivWanpra);
//            rv_cell_event.setViewVisibility(R.id.imageView1, View.VISIBLE);
            rv_cell_event.setViewVisibility(R.id.v_top_divider, View.VISIBLE);
            rv_cell_event.setTextViewText(R.id.textView1,
                    data.getString(CalendarThaiAction.IMPORTANT_DESC));
            rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);
            rv_cell_event.setTextColor(R.id.textView1,
                    context.getResources().getInteger(R.color.day_in_holiday));

            rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
            maxView--;
        }
        if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
            rv_cell_event = new RemoteViews(context.getPackageName(),
                    R.layout.cell_event);
//            rv_cell_event.setImageViewResource(R.id.imageView1, R.id.ivWanpra);
//            rv_cell_event.setViewVisibility(R.id.imageView1, View.VISIBLE);

            rv_cell_event.setTextViewText(R.id.textView1,
                    data.getString(CalendarThaiAction.IMPORTANT_DESC));
            rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);
            rv_cell_event.setTextColor(R.id.textView1,
                    context.getResources().getInteger(R.color.day_in_important));

            rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
            maxView--;
        }
//        //wanpra
//        if (data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
//            rv_cell_event = new RemoteViews(context.getPackageName(),
//                    R.layout.cell_event);
//            rv_cell_event.setImageViewResource(R.id.imageView1, R.drawable.ic_wanpra_xx);
//            rv_cell_event.setViewVisibility(R.id.imageView1, View.VISIBLE);
//
//            rv_cell_event.setTextViewText(R.id.textView1,
//                    context.getResources().getString(R.string.txt_wanpra));
//            rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);
//
//            rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
//            maxView--;
//        }
        String[] mWidgetItems = {
//                "Aerith Gainsborough Aerith Gainsborough Aerith Gainsborough"
//                , "Barret Wallace"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
        };
        int[] resId = {
                R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
                , R.drawable.ic_wanpra_xx
        };


        for(int i=0;i<mWidgetItems.length && i<maxView;i++) {
            rv_cell_event = new RemoteViews(context.getPackageName(),
                    R.layout.cell_event);

            rv_cell_event.setImageViewResource(R.id.imageView1, resId[i]);
            rv_cell_event.setViewVisibility(R.id.imageView1, View.VISIBLE);
            rv_cell_event.setTextViewText(R.id.textView1, "19:00 " + mWidgetItems[i]);
            rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);

            rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
        }
        //more event
        if(mWidgetItems.length > maxView){
            rv_cell_event = new RemoteViews(context.getPackageName(),
                    R.layout.cell_event);

            rv_cell_event.setTextViewText(R.id.textView1,
                    "อ่านต่อ >>");
            rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);
            rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
        }

        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_event);

        //set calendar background color
//        rv_activity_calendar_thai.setInt(R.id.llCalendarContainer, "setBackgroundColor",
//                context.getResources().getInteger(R.color.none_background));

        appWidgetManager.updateAppWidget(appWidgetId, rv_activity_calendar_thai);
    }

    private void drawWidgetListMonth(Context context, int appWidgetId) {
        Log.d("drawWidgetListMonth appWidgetId", "" + appWidgetId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));

        RemoteViews rv_activity_calendar_thai;
        rv_activity_calendar_thai = new RemoteViews(context.getPackageName(), R.layout.activity_calendar_thai);
        rv_activity_calendar_thai.removeAllViews(R.id.llCalendarContainer);
        //set calendar background color
        rv_activity_calendar_thai.setInt(R.id.llCalendarContainer, "setBackgroundColor",
                sharedPrefs.getInt(context.getString(R.string.key_background_widget),
                        context.getResources().getInteger(R.integer.COLOR_BACKGROUND_CALENDAR)));

        //month bar
        RemoteViews rv_row_month_bar = new RemoteViews(context.getPackageName(), R.layout.row_month_bar);
        rv_row_month_bar.setTextViewText(R.id.tvTitleMonth, String.valueOf(CalendarUtils.getYearTh(cal.get(Calendar.YEAR))));
        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_month_bar);

        //Days of Month
        RemoteViews rv_row_calendar = new RemoteViews(context.getPackageName(), R.layout.row_calendar);
        int row_month = 4;
        int index_month = 0;
        for (int row = 0; row < row_month; row++) {
            RemoteViews rv_row_month = new RemoteViews(context.getPackageName(), R.layout.row_month);
            for (int col = 0; col < CalendarUtils.MONTHNAMETH.length/row_month; col++){
                RemoteViews rv_cell_month = new RemoteViews(context.getPackageName(), R.layout.cell_month);
                rv_cell_month.setTextViewText(R.id.tvMonthName, String.valueOf(CalendarUtils.MONTHNAMETH[index_month]));
                if(index_month == cal.get(Calendar.MONTH)) {//to month
                    rv_cell_month.setInt(R.id.layCellMonthContainer, "setBackgroundColor",
                            context.getResources().getInteger(R.color.today_background));
                }
                //btn control
                rv_cell_month.setOnClickPendingIntent(R.id.layCellMonthContainer,
                        PendingIntent.getBroadcast(context, Integer.parseInt(appWidgetId + "" + cal.get(Calendar.YEAR) + "" + index_month),
                                new Intent(context, getClass())
                                        .setAction(CalendarThaiAction.ACTION_LIST_DAY)
                                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                        .putExtra(CalendarThaiAction.EXTRA_MONTH_DETAIL, index_month),
                                PendingIntent.FLAG_UPDATE_CURRENT));

                rv_row_month.addView(R.id.linLayRowMonthContainer, rv_cell_month);
                index_month++;
            }// End Colume
            rv_row_calendar.addView(R.id.llRowCalendarContainer, rv_row_month);
        }// End Row

        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_calendar);

        //btn control
        if(cal.get(Calendar.YEAR) > context.getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvPrevMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_PREVIOUS_YEAR)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvPrevMonth, View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < context.getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvNextMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_NEXT_YEAR)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvNextMonth, View.INVISIBLE);
        }
        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvCurrMonth,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvTitleMonth,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_LIST_YEAR)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, rv_activity_calendar_thai);
    }

    private void drawWidgetListYear(Context context, int appWidgetId) {
        Log.d("drawWidgetListYear appWidgetId", "" + appWidgetId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_DATE + appWidgetId, 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_MONTH + appWidgetId, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.WIDGET_PREF_YEAR + appWidgetId, cal.get(Calendar.YEAR)));

        RemoteViews rv_activity_calendar_thai;
        rv_activity_calendar_thai = new RemoteViews(context.getPackageName(), R.layout.activity_calendar_thai);
        rv_activity_calendar_thai.removeAllViews(R.id.llCalendarContainer);
        //set calendar background color
        rv_activity_calendar_thai.setInt(R.id.llCalendarContainer, "setBackgroundColor",
                sharedPrefs.getInt(context.getString(R.string.key_background_widget),
                        context.getResources().getInteger(R.integer.COLOR_BACKGROUND_CALENDAR)));

        //show 20 col
        int numOfYear = 20;
        int rowOfYear = 5;
        int colOfYear = numOfYear/rowOfYear;
        int roundOfYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR)) % numOfYear;
        int beginYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR)) - roundOfYear;
        int toYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR));

//        Log.d("colOfYear",""+colOfYear);
//        Log.d("roundOfYear",""+roundOfYear);
//        Log.d("beginYear",""+beginYear);
//        Log.d("toYear",""+toYear);

        //month bar
        RemoteViews rv_row_month_bar = new RemoteViews(context.getPackageName(), R.layout.row_month_bar);
        rv_row_month_bar.setTextViewText(R.id.tvTitleMonth, String.valueOf(beginYear)+" - "+String.valueOf(beginYear+numOfYear-1));
        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_month_bar);

        //Days of Month
        RemoteViews rv_row_calendar = new RemoteViews(context.getPackageName(), R.layout.row_calendar);

        for (int row = 0; row < rowOfYear; row++) {
            RemoteViews rv_row_year = new RemoteViews(context.getPackageName(), R.layout.row_year);
            for (int col = 0; col < colOfYear; col++){
                RemoteViews rv_cell_month = new RemoteViews(context.getPackageName(), R.layout.cell_year);
                rv_cell_month.setTextViewText(R.id.tvYearName, String.valueOf(beginYear));
                if(beginYear == toYear) {
                    rv_cell_month.setInt(R.id.layCellYearContainer, "setBackgroundColor",
                            context.getResources().getInteger(R.color.today_background));
                }
                //btn control
                rv_cell_month.setOnClickPendingIntent(R.id.layCellYearContainer,
                        PendingIntent.getBroadcast(context, Integer.parseInt(appWidgetId + "" + CalendarUtils.getYearEn(beginYear)),
                                new Intent(context, getClass())
                                        .setAction(CalendarThaiAction.ACTION_LIST_MONTH)
                                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                        .putExtra(CalendarThaiAction.EXTRA_YEAR_DETAIL, CalendarUtils.getYearEn(beginYear))
                                ,
                                PendingIntent.FLAG_UPDATE_CURRENT));

                rv_row_year.addView(R.id.linLayRowYearContainer, rv_cell_month);
                beginYear++;
            }// End Colume
            rv_row_calendar.addView(R.id.llRowCalendarContainer, rv_row_year);
        }// End Row

        rv_activity_calendar_thai.addView(R.id.llCalendarContainer, rv_row_calendar);

        //btn control
        if(cal.get(Calendar.YEAR) > context.getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvPrevMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_PREVIOUS_YEAR_GROUP)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                    .putExtra(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP, numOfYear),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvPrevMonth, View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < context.getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvNextMonth,
                    PendingIntent.getBroadcast(context, appWidgetId,
                            new Intent(context, getClass())
                                    .setAction(CalendarThaiAction.ACTION_NEXT_YEAR_GROUP)
                                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                    .putExtra(CalendarThaiAction.WIDGET_NUM_YEAR_GROUP, numOfYear),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }else{
            rv_activity_calendar_thai.setViewVisibility(R.id.tvNextMonth, View.INVISIBLE);
        }
        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvCurrMonth,
                PendingIntent.getBroadcast(context, appWidgetId,
                        new Intent(context, getClass())
                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                        PendingIntent.FLAG_UPDATE_CURRENT));

//        rv_activity_calendar_thai.setOnClickPendingIntent(R.id.tvTitleMonth,
//                PendingIntent.getBroadcast(context, appWidgetId,
//                        new Intent(context, getClass())
//                                .setAction(CalendarThaiAction.ACTION_LIST_DAY)
//                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
//                        PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, rv_activity_calendar_thai);
    }
}
