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
    @Override
    protected void onCreate(Bundle data) {
        super.onCreate(data);
        Log.d("CalendarThaiActivity onCreate data", "" + data);
        context = getApplicationContext();
        setTitle(R.string.app_name);
        setContentView(R.layout.calendarthai_main_activity);
        LinearLayout linearLayout_container = (LinearLayout) findViewById(R.id.container);
        linearLayout_container.setBackgroundColor(getResources().getColor(R.color.foreground));

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
        LinearLayout linearLayout_container = (LinearLayout) findViewById(R.id.container);
        linearLayout_container.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout linearLayout_calendarthai = (LinearLayout) inflater.inflate(
                R.layout.calendarthai, null);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            sharedPrefs.edit()
                    .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                    .apply();
        } else {
            sharedPrefs.edit()
                    .putBoolean(CalendarThaiAction.ACTION_DAY_DETAIL, false)
                    .commit();
        }

        MonthView monthView = new MonthView();
        monthView.drawMonth(context, linearLayout_calendarthai, sharedPrefs);

        LinearLayout.LayoutParams params;

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        linearLayout_container.addView(linearLayout_calendarthai, params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2f);
        LinearLayout linearLayout_row_event = (LinearLayout) inflater.inflate(
                R.layout.row_event, null);
        linearLayout_container.addView(linearLayout_row_event, params);
    }
}
