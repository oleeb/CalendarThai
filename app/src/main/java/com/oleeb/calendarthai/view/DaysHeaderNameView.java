package com.oleeb.calendarthai.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.oleeb.calendarthai.R;

/**
 * Created by j1tth4 on 6/29/14.
 */
public class DaysHeaderNameView {
    public static LinearLayout drawDaysHeaderName(Context context, String[] daysName){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout_row_header = (LinearLayout)
                inflater.inflate(R.layout.row_header, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        for(String days : daysName) {
            LinearLayout linearLayout_cell_header = (LinearLayout)
                    inflater.inflate(R.layout.cell_header, null);
            TextView tvDayName = (TextView)linearLayout_cell_header.findViewById(R.id.tvDayName);
            tvDayName.setText(days);
            linearLayout_row_header.addView(linearLayout_cell_header, params);
        }
        return linearLayout_row_header;
    }
}
