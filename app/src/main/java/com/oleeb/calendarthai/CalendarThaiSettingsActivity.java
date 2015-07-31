package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.oleeb.calendarthai.action.CalendarThaiAction;

public class CalendarThaiSettingsActivity extends PreferenceActivity  implements OnSharedPreferenceChangeListener {
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("CalendarThai Settings");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            addPreferencesFromResource(R.xml.calendarthai_settings);
        }else{
            addPreferencesFromResource(R.xml.calendarthai_settings_v8);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendarthai_done, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_done:
                Log.d("CalendarThaiSettingsActivity onOptionsItemSelected MenuItem", "" + item.getItemId());
                onDone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    public void onDone() {
//        Log.d("CalendarThaiSettingsActivity onBackPressed Intent Action",""+CalendarThaiAction.ACTION_APPWIDGET_UPDATE.equals(getIntent().getAction()));
//        if (CalendarThaiAction.ACTION_APPWIDGET_UPDATE.equals(getIntent().getAction())) {
//            Intent intent = getIntent();
//            Bundle extras = intent.getExtras();
//            Log.d("CalendarThaiSettingsActivity onBackPressed extras",""+extras);
//            if (extras != null) {
//                int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//                Intent result = new Intent();
//                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
//                setResult(RESULT_OK, result);	// This will NOT trigger ACTION_APPWIDGET_UPDATE, better send a separate broadcast
                this.getApplicationContext().sendBroadcast(new Intent(CalendarThaiAction.ACTION_SETTING));
//            }
//        } else {  // TODO: look for a more elegant solution than this
//            this.getApplicationContext().sendBroadcast(new Intent(CalendarThaiAction.ACTION_APPWIDGET_UPDATE));
//        }
        finish();
    }
}
