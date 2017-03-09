package com.oleeb.calendarthai.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.receiver.CalendarThaiReceiver;
import com.oleeb.calendarthai.service.CalendarThaiSyncDataService;

import java.util.Calendar;

/**
 * Created by HackerOne on 10/10/2015.
 */
public class CalendarThaiAlarmManager {
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    public static void startAlarmManager(Context context){
        Log.d("" + CalendarThaiAlarmManager.class, "is null :"+(alarmMgr == null));
        if(alarmMgr == null) {
            //call ServiceIntent CalendarThaiSyncData
            Intent mServiceIntent = new Intent(context, CalendarThaiSyncDataService.class);
            context.startService(mServiceIntent);
            // Set the alarm to start at approximately x:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 5);
            calendar.set(Calendar.MINUTE, 1);

            Intent intent = new Intent(context, CalendarThaiReceiver.class);
            intent.setAction(CalendarThaiAction.ACTION_NOTIFICATION);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            Log.d("" + CalendarThaiAlarmManager.class, "TimeInMillis :"+(calendar.getTimeInMillis()));
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
//            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    1000*60*60*24, alarmIntent);
//            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    1000*60*2, alarmIntent);
        }
    }
}
