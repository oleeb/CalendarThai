package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.view.MonthView;

import java.util.Calendar;

/**
 * Created by HackerOne on 5/17/2015.
 */
@SuppressLint("LongLogTag")
public class CalendarThaiActivity extends Activity {
    protected Context context;
    private Bundle extras;
    @Override
    protected void onCreate(Bundle data) {
        super.onCreate(data);
        Log.d("CalendarThaiActivity onCreate data", "" + data);
        context = getApplicationContext();
        setTitle(R.string.app_name);
        setContentView(R.layout.calendarthai_main_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings, true);
        }else{
            PreferenceManager.setDefaultValues(context, R.xml.calendarthai_settings_v8, true);
        }
        drawCalendar();
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
        Log.d("CalendarThaiActivity onResume", "");
        drawCalendar();
    }

    private void drawCalendar() {
//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);;
//        LinearLayout linearLayout_container = (LinearLayout)
//                inflater.inflate(R.layout.calendarthai_main_activity, null);

        LinearLayout linearLayout_container = (LinearLayout) findViewById(R.id.container);
        linearLayout_container.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout linearLayout_calendarthai = (LinearLayout)inflater.inflate(
                R.layout.calendarthai, null);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(extras != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, true)
                        .commit();
            }
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
            MonthView.drawWidgetMonth(context, linearLayout_calendarthai, sharedPrefs);
        }

        LinearLayout.LayoutParams params;

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        linearLayout_container.addView(linearLayout_calendarthai, params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2f);
        LinearLayout linearLayout_row_event = (LinearLayout)inflater.inflate(
                R.layout.row_event, null);
        linearLayout_container.addView(linearLayout_row_event, params);
    }

    public void drawCalendar(String action, Bundle extras) {
        this.context = context;
        this.extras = extras;
        Log.d("CalendarThaiWidget onReceive action", "" + action);
        Log.d("CalendarThaiWidget onReceive extras", "" + extras);
        SharedPreferences sharedPrefs = null;
        if (CalendarThaiAction.ACTION_SETTING.equals(action)){

        } else if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
        } else if (CalendarThaiAction.ACTION_NEXT_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            int thisMonth = sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
        } else if (CalendarThaiAction.ACTION_RESET_MONTH.equals(action)) {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs.edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .apply();
            } else {
                sharedPrefs.edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .commit();
            }
        } else if(CalendarThaiAction.ACTION_LIST_DAY.equals(action)){

        } else if (CalendarThaiAction.ACTION_DAY_DETAIL.equals(action)) {

        }
        drawCalendar();
    }
}
