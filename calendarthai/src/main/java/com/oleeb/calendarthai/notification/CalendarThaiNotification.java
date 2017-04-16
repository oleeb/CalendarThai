package com.oleeb.calendarthai.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.oleeb.calendarthai.CalendarThai;
import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.DataOfDayDao;
import com.oleeb.calendarthai.dto.DataOfDayDto;

import java.util.Calendar;

/**
 * Created by HackerOne on 10/3/2015.
 */
public class CalendarThaiNotification {
    public static void createNotification(Context context) {
        //Log.d("" + CalendarThaiNotification.class, "createNotification");
        Calendar calendar = Calendar.getInstance();
        DataOfDayDao dataOfDayDao = new DataOfDayDao(context, calendar);
        DataOfDayDto dataOfDayDto = dataOfDayDao.getData();
//        //Log.d("" + CalendarThaiNotification.class, "mapDays:"+mapDays);

        if (dataOfDayDto != null && dataOfDayDto.getData() != null) {
            Bundle mapDays = dataOfDayDto.getData();
//          Notification of Wanpra
            if(mapDays.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                //reset for current month
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .remove(CalendarThaiAction.PREF_DATE)
                            .remove(CalendarThaiAction.PREF_MONTH)
                            .remove(CalendarThaiAction.PREF_YEAR)
                            .apply();
                } else {
                    sharedPrefs
                            .edit()
                            .remove(CalendarThaiAction.PREF_DATE)
                            .remove(CalendarThaiAction.PREF_MONTH)
                            .remove(CalendarThaiAction.PREF_YEAR)
                            .commit();
                }
                Intent intent = new Intent(context, CalendarThai.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//                //Log.d("" + CalendarThaiNotification.class, "createNotification Wanpra....");
                String str_wax = (mapDays.getString(CalendarThaiAction.WAXING)
                        .equals(CalendarThaiAction.WAXING) ?
                        context.getResources().getString(R.string.txt_waxing) :
                        context.getResources().getString(R.string.txt_waning))
                        + " " + mapDays.getString(CalendarThaiAction.WAXING_DAY) + " "
                        + context.getResources().getString(R.string.txt_subfix)
                        + " " + context.getResources().getString(R.string.txt_month) + " "
                        + mapDays.get(CalendarThaiAction.WAXING_MONTH_2);

                Notification notification =
                        new NotificationCompat.Builder(context)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setSmallIcon(R.drawable.ic_wanpra_xx)
                                .setContentText(context.getResources().getString(R.string.txt_today)
                                        + context.getResources().getString(R.string.txt_wanpra) + " "
                                        + str_wax)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent)
                                .build();

                notification.defaults |= Notification.DEFAULT_SOUND; // Sound
                notification.defaults |= Notification.DEFAULT_VIBRATE; // Vibrate
                notification.defaults |= Notification.DEFAULT_LIGHTS; // Lights

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
            }

//          Notification of Calendar Holiday
            if(mapDays.get(CalendarThaiAction.IMPORTANT_DESC) != null) {
                //reset for current month
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .remove(CalendarThaiAction.PREF_DATE)
                            .remove(CalendarThaiAction.PREF_MONTH)
                            .remove(CalendarThaiAction.PREF_YEAR)
                            .apply();
                } else {
                    sharedPrefs
                            .edit()
                            .remove(CalendarThaiAction.PREF_DATE)
                            .remove(CalendarThaiAction.PREF_MONTH)
                            .remove(CalendarThaiAction.PREF_YEAR)
                            .commit();
                }
                Intent intent = new Intent(context, CalendarThai.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//                //Log.d("" + CalendarThaiNotification.class, "createNotification Holiday....");
                Notification notification =
                        new NotificationCompat.Builder(context)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentText(mapDays.getString(CalendarThaiAction.IMPORTANT_DESC))
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent)
                                .build();

                notification.defaults |= Notification.DEFAULT_SOUND; // Sound
                notification.defaults |= Notification.DEFAULT_VIBRATE; // Vibrate
                notification.defaults |= Notification.DEFAULT_LIGHTS; // Lights

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(2, notification);
            }

//          Notification of Calendar Event
            if(mapDays.get(CalendarThaiAction.IMPORTANT_DESC) != null) {

            }
        }
    }

}
