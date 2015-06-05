package com.oleeb.calendarthai;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.view.MonthView;

import java.util.Calendar;

/**
 * Created by HackerOne on 5/17/2015.
 */
public class CalendarThaiActivity extends Activity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.calendarthai_activity);

        ImageButton btnSettings = (ImageButton) findViewById(R.id.btn_settings);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_settings:
                        Intent intentCalendarThaiSettingsActivity = new Intent(getApplicationContext(),
                                CalendarThaiSettingsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//                        bun.putString("userid","aaa");
//                        bun.putString("deviceid", "bbb");
                        intentCalendarThaiSettingsActivity.setAction(CalendarThaiAction.ACTION_APPWIDGET_UPDATE);
                        intentCalendarThaiSettingsActivity.putExtras(extras);
                        startActivity(intentCalendarThaiSettingsActivity);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        btnSettings.setOnClickListener(listener);

//        drawCalendar(this);
    }

//    private void drawCalendar(Context context) {
//        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);
//
//        rv = MonthView.drawWeeks(context, rv);
//
//        rv.setViewVisibility(R.id.month_bar, View.VISIBLE);
//
//        rv.setViewVisibility(R.id.prev_month_button, View.VISIBLE);
//        rv.setOnClickPendingIntent(R.id.prev_month_button,
//                PendingIntent.getBroadcast(context, 0,
//                        new Intent(context, CalendarThaiWidget.class)
//                                .setAction(CalendarThaiAction.ACTION_PREVIOUS_MONTH),
//                        PendingIntent.FLAG_UPDATE_CURRENT));
//
//        rv.setViewVisibility(R.id.next_month_button, View.VISIBLE);
//        rv.setOnClickPendingIntent(R.id.next_month_button,
//                PendingIntent.getBroadcast(context, 0,
//                        new Intent(context, CalendarThaiWidget.class)
//                                .setAction(CalendarThaiAction.ACTION_NEXT_MONTH),
//                        PendingIntent.FLAG_UPDATE_CURRENT));
//
//        rv.setOnClickPendingIntent(R.id.month_label,
//                PendingIntent.getBroadcast(context, 0,
//                        new Intent(context, CalendarThaiWidget.class)
//                                .setAction(CalendarThaiAction.ACTION_RESET_MONTH),
//                        PendingIntent.FLAG_UPDATE_CURRENT));
//
//        //appWidgetManager.updateAppWidget(appWidgetId, rv);
//    }
}
