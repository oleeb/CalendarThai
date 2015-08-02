package com.oleeb.calendarthai.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.oleeb.calendarthai.R;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class DaysHeaderNameViewWidget {
    public static RemoteViews drawDaysHeaderName(Context context, String[] daysName){
        RemoteViews headerRowRv = new RemoteViews(context.getPackageName(), R.layout.row_header);
        for(String days : daysName) {
            RemoteViews dayRv = new RemoteViews(context.getPackageName(), R.layout.cell_header);
            dayRv.setTextViewText(R.id.tvDayName, days);
            headerRowRv.addView(R.id.row_head_container, dayRv);
        }
        return headerRowRv;
    }
}
