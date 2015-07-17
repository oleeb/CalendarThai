package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.view.MonthView;
import com.oleeb.calendarthai.view.WeekView;

import java.util.Calendar;

/**
 * Created by HackerOne on 5/17/2015.
 */
@SuppressLint("LongLogTag")
public class CalendarThaiActivity extends Activity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setTitle(R.string.app_name);

        Log.d("CalendarThaiWidget onCreate savedInstanceState", "" + savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings, true);
        }else{
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings_v8, true);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);
/*        if(data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .commit();
            }
            rv = WeekView.drawWidgetDayDetail(context, rv, sharedPrefs, data, getClass());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                        .commit();
            }
            rv = MonthView.drawWidgetMonth(context, rv, sharedPrefs, getClass());
        }
*/
        rv = MonthView.drawWidgetMonth(context, rv, sharedPrefs, getClass());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        setContentView(rv.apply(context, null),params);

        addContentView(rv.apply(context, null),params);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
