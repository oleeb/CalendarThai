package com.oleeb.calendarthai.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.CalendarThaiWidget;
import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.dto.DaysOfWeekDto;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class WeekView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RemoteViews drawDays(Context context, int appWidgetId, DaysOfWeekDto daysOfWeekDto, int week, SharedPreferences sharedPrefs){
        RemoteViews rowWeekRv = new RemoteViews(context.getPackageName(), R.layout.row_week);
        for(int i = 0; i < daysOfWeekDto.getMaximumDaysOfWeek(); i++){
            final Days days = daysOfWeekDto.daysOfWeek.get(i);
            RemoteViews rowDayRv = new RemoteViews(context.getPackageName(), R.layout.row_day);

            rowDayRv.setOnClickPendingIntent(R.id.layRowDayContainer,
                    PendingIntent.getBroadcast(context, Integer.parseInt(week + "" + i),
                            new Intent(context, CalendarThaiWidget.class)
                                    .setAction(CalendarThaiAction.ACTION_DAY_DETAIL)
                                    .putExtras(days.data)
                                    .putExtra(CalendarThaiAction.APP_WIDGET_ID, appWidgetId),
                            PendingIntent.FLAG_UPDATE_CURRENT));

            rowWeekRv.addView(R.id.row_week_container, WeekView.drawDayDetail(context, rowDayRv, sharedPrefs, days.data));
        }
        return rowWeekRv;
    }

    public static RemoteViews drawDayDetail(Context context, RemoteViews rowDayRv, SharedPreferences sharedPrefs, Bundle data) {

        String str_wax = (data.get(CalendarThaiAction.WAXING).equals("WX") ? "ขึ้น " : "แรม ")
                + data.get(CalendarThaiAction.WAXING_DAY) + " ค่ำ "
                + "เดือน " + data.get(CalendarThaiAction.WAXING_MONTH_2);

        rowDayRv.setTextViewText(R.id.tvDay, data.get(CalendarThaiAction.DAY).toString());
        if(sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_WAX,true)) {
            rowDayRv.setTextViewText(R.id.tvWax, str_wax );
        }
        if (data.getBoolean(CalendarThaiAction.TO_MONTH)) { // in Month
            if (data.getBoolean(CalendarThaiAction.TO_DAY)) { // to Day
                rowDayRv.setInt(R.id.layRowDayContainer,"setBackgroundColor", sharedPrefs.getInt(CalendarThaiAction.TO_DAY_BACKGROUND_COLOR, R.integer.TODAY_BACKGROUND));
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.TO_DAY_COLOR, R.integer.TODAY));
            }else if (data.getInt(CalendarThaiAction.DAY_IN_WEEK) == 0 || (sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_HOLIDAY, true) && data.get(CalendarThaiAction.HOLIDAY) != null)) { //in Sunday and Holiday
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.DAY_IN_HOLIDAY));
                if(sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_HOLIDAY, true) && data.get(CalendarThaiAction.HOLIDAY) != null) {//set color in Holiday
                    rowDayRv.setTextViewText(R.id.tvHoliday, data.get(CalendarThaiAction.HOLIDAY).toString());
                    rowDayRv.setTextColor(R.id.tvHoliday, sharedPrefs.getInt(CalendarThaiAction.HOLIDAY_COLOR, R.integer.DAY_IN_HOLIDAY));
                    rowDayRv.setViewVisibility(R.id.tvHoliday, View.VISIBLE);
                }
            }else{
                rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.DAY_IN_MONTH));
            }
            /***********Buddha Moon Phase Day***************/
            if(sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_WAX,true)) {
                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.DAY_IN_MONTH));
            }
            if(sharedPrefs.getBoolean(CalendarThaiAction.SHOW_IMG_WANPRA,true) && data.get(CalendarThaiAction.WAXING) != null) {
                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.TO_MONTH_COLOR, R.integer.DAY_IN_MONTH));
                //in WanPhra
                if (data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                    rowDayRv.setImageViewResource(R.id.iv_wanpra, R.drawable.ic_wanpra);
                    rowDayRv.setViewVisibility(R.id.iv_wanpra, View.VISIBLE);

                }
            }
        }else{
            rowDayRv.setTextColor(R.id.tvDay, sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.DAY_OTHER_MONTH));
            if(sharedPrefs.getBoolean(CalendarThaiAction.SHOW_TXT_WAX,true)) {
                rowDayRv.setTextColor(R.id.tvWax, sharedPrefs.getInt(CalendarThaiAction.OTHER_MONTH_COLOR, R.integer.DAY_OTHER_MONTH));
            }
        }

        return rowDayRv;
    }
}