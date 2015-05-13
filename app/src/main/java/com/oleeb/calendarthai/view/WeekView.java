package com.oleeb.calendarthai.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

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
            rowDayRv.setTextViewText(R.id.text1, days.data.get(Days.DAY).toString());

            if ((Boolean) days.data.get(Days.TO_MONTH)) { // in Month
                rowDayRv.setInt(R.id.row_day_container,"setBackgroundColor", R.color.background_calendar);
                if ((Boolean)days.data.get(Days.TO_DAY)) { // to Day
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
                        rowDayRv.setViewVisibility(R.id.wanpra, View.VISIBLE);
//                        ImageView imgBuddha = new ImageView(context);
//                        imgBuddha.setImageResource(R.drawable.ic_wanpra);
//                        RelativeLayout.LayoutParams params = getParams();
//                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                        rowDayRv.addView(R.id.row_day_container, params);
                    }
                    if (i == 0) { //in Sunday
//                        tvDaysNum.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                /*****************Holiday*************************/
//                if(days.data.get(Days.HOLIDAY) != null){
//                    tvDaysNum.setTextColor(Color.RED);
//                }
            }else{
                rowDayRv.setTextColor(R.id.text1, R.color.day);
            }

            rowWeekRv.addView(R.id.row_week_container, rowDayRv);
        }
        return rowWeekRv;
    }
    public static RelativeLayout.LayoutParams getParams(){
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}