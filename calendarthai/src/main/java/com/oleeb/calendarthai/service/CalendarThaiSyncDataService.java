package com.oleeb.calendarthai.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.oleeb.calendarthai.syncdata.CalendarThaiSyncData;

/**
 * Created by HackerOne on 10/23/2015.
 */
public class CalendarThaiSyncDataService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            //Log.d(this.getClass().getName(), "services start");
            super.onCreate();
            CalendarThaiSyncData calendarThaiSyncData = new CalendarThaiSyncData(this);
            Thread thread = new Thread(calendarThaiSyncData, "CalendarThaiSyncData");
            thread.start();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
}
