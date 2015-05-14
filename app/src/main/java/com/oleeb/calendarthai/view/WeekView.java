package com.oleeb.calendarthai.view;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.dto.DaysOfWeekDto;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class WeekView {
    public static RemoteViews drawDays(Context context, DaysOfWeekDto daysOfWeekDto){
        RemoteViews rowWeekRv = new RemoteViews(context.getPackageName(), R.layout.row_week);
        for(int i = 0; i < daysOfWeekDto.getMaximumDaysOfWeek(); i++){
            final Days days = daysOfWeekDto.daysOfWeek.get(i);

            RemoteViews rowDayRv = new RemoteViews(context.getPackageName(), R.layout.row_day);
            rowDayRv.setTextViewText(R.id.tvDay, days.data.get(Days.DAY).toString());
            rowDayRv.setTextViewText(R.id.tvWanpra,
                            (days.data.get(Days.WAXING).equals("WX") ? "ขึ้น " : "แรม ") + days.data.get(Days.WAXING_DAY) + " ค่ำ "
                            + " เดือน " + days.data.get(Days.WAXING_MONTH_2)
                    );

            if ((Boolean) days.data.get(Days.TO_MONTH)) { // in Month
                if ((Boolean)days.data.get(Days.TO_DAY)) { // to Day
                    rowDayRv.setInt(R.id.layRowDayContainer,"setBackgroundColor", context.getResources().getColor(R.color.background_calendar));
                    if(i == 0){
                        //rowDayRv.setTextColor(R.id.text1, R.color.red_light);
                    }else {
                        //rowDayRv.setTextColor(R.id.text1, R.color.today);
                    }
                }else if (i == 0) { //in Sunday
                    //rowDayRv.setTextColor(R.id.text1, R.color.red_light);
                }else{
                    //rowDayRv.setInt(R.id.text1, "setTextColor", R.color.white);
                }
                /***********Buddha Moon Phase Day***************/
                if(days.data.get(Days.WAXING) != null) {
                    //in WanPhra
                    if ((Boolean)days.data.get(Days.WAXING_WANPRA)) {
                        rowDayRv.setViewVisibility(R.id.iv_wanpra, View.VISIBLE);
                    }
                    if (i == 0) { //in Sunday
//                        tvDaysNum.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                /*****************Holiday*************************/
                if(days.data.get(Days.HOLIDAY) != null){
                    rowDayRv.setTextViewText(R.id.tvHoliday, days.data.get(Days.HOLIDAY).toString());
                    rowDayRv.setTextColor(R.id.tvDay, context.getResources().getColor(R.color.day_in_holiday));
                    rowDayRv.setViewVisibility(R.id.tvHoliday, View.VISIBLE);
                }
            }else{
                rowDayRv.setTextColor(R.id.tvDay, context.getResources().getColor(R.color.day_other_month));
            }

            rowWeekRv.addView(R.id.row_week_container, rowDayRv);
        }
        return rowWeekRv;
    }
    public static RelativeLayout.LayoutParams getParams(){
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}