package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    protected void onCreate(Bundle data) {
        super.onCreate(data);
        Log.d("CalendarThaiActivity onCreate data", "" + data);
        context = getApplicationContext();
        setTitle(R.string.app_name);
        //setContentView(R.layout.calendarthai_main_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings, true);
        }else{
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings_v8, true);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendarthai);

        if(data != null) {
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

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.calendarthai_main_activity, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container);

        LinearLayout.LayoutParams params;

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        linearLayout.addView(rv.apply(context, null), params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2f);

        RemoteViews rv1 = new RemoteViews(context.getPackageName(), R.layout.row_event);

        linearLayout.addView(rv1.apply(context, null),params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        setContentView(linearLayout,params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendarthai_setting, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Log.d("CalendarThaiActivity onOptionsItemSelected MenuItem", "" + item.getItemId());
                Intent intent = new Intent(this, CalendarThaiSettingsActivity.class);
                this.startActivity(intent);
                //this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
