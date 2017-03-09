package com.oleeb.calendarthai.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.alarm.CalendarThaiAlarmManager;
import com.oleeb.calendarthai.notification.CalendarThaiNotification;

/**
 * Created by HackerOne on 5/17/2015.
 */
@SuppressLint("LongLogTag")
public class CalendarThaiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(""+CalendarThaiReceiver.class,"get Action:"+action);
        if(action.equals(Intent.ACTION_INSTALL_PACKAGE)
                || action.equals(Intent.ACTION_MY_PACKAGE_REPLACED)
                || action.equals(Intent.ACTION_PACKAGE_ADDED)
                || action.equals(Intent.ACTION_PACKAGE_REPLACED)){
            //set Schedule Alarm
            CalendarThaiAlarmManager.startAlarmManager(context);
        }else if(action.equals(CalendarThaiAction.ACTION_NOTIFICATION)){
            CalendarThaiNotification.createNotification(context);
        }else if(action.equals(intent.ACTION_PACKAGE_REMOVED)
                || action.equals(intent.ACTION_UNINSTALL_PACKAGE)){

        }
    }

}
