package com.oleeb.calendarthai.syncdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.oleeb.calendarthai.R;
import com.oleeb.calendarthai.buddhamoonphase.BuddhaMoonPhaseDatabase;
import com.oleeb.calendarthai.holiday.HolidayDatabase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HackerOne on 10/26/2015.
 */
public class CalendarThaiSyncData implements Runnable {
    Context context;
    public CalendarThaiSyncData(Context context){
        this.context = context;
    }

    @SuppressLint("LongLogTag")
    private static void insertBuddhMoonPhase(Context context, ZipInputStream zis){
        BuddhaMoonPhaseDatabase.BuddhaMoonPhaseHelper buddhaMoonPhaseHelper = new BuddhaMoonPhaseDatabase.BuddhaMoonPhaseHelper(context);
        SQLiteDatabase db = buddhaMoonPhaseHelper.getWritableDatabase();
        buddhaMoonPhaseHelper.onUpgrade(db, 1, 1);
        Scanner sc = new Scanner(zis);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
//            //Log.d("CalendarThaiSyncDataService", "onHandleIntent Begin load BuddhaMoonPhaseDatabase:" + line);
            String[] strings = TextUtils.split(line, ",");
            long insert = buddhaMoonPhaseHelper.insertBuddhMoonPhase(strings[0].trim(), strings[1].trim(),
                    strings[2].trim(), strings[3].trim(), strings[4].trim());
        }
        if (db != null) db.close();

    }

    @SuppressLint("LongLogTag")
    private static void insertHoliday(Context context, ZipInputStream zis){
        HolidayDatabase.HolidayHelper holidayHelper = new HolidayDatabase.HolidayHelper(context);
        SQLiteDatabase db = holidayHelper.getWritableDatabase();
        holidayHelper.onUpgrade(db, 1, 1);

        Scanner sc = new Scanner(zis);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
//            //Log.d("CalendarThaiSyncDataService", "onHandleIntent Begin load HolidayDatabase:" + line);
            String[] strings = TextUtils.split(line, ",");
            long insert = holidayHelper.insertHoliday(strings[0].trim(), strings[1].trim(),
                    strings[2].trim(), strings[3].trim());
        }
        if (db != null) db.close();
    }

    private static boolean isInternetAvailable(String url) {
        try {
            if (InetAddress.getByName(url).equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        //Log.d("CalendarThaiSyncDataService", "onHandleIntent");
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(this.context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Log.d("CalendarThaiSyncDataService","onHandleIntent networkInfo:"+(networkInfo != null));
        //Log.d("CalendarThaiSyncDataService","onHandleIntent networkInfo isConnected:"+networkInfo.isConnected());
        //Log.d("CalendarThaiSyncDataService", "onHandleIntent isInternetAvailable:" + isInternetAvailable(this.context.getResources().getString(R.string.chk_inet_url)));
        //Log.d("CalendarThaiSyncDataService","onHandleIntent Internet isConnected:"+(networkInfo != null && networkInfo.isConnected() && isInternetAvailable(this.context.getResources().getString(R.string.chk_inet_url))));
        if (networkInfo != null && networkInfo.isConnected() && isInternetAvailable(this.context.getResources().getString(R.string.chk_inet_url))) {
            ZipInputStream zis;
            OkHttpClient client;
            String url;
            Request request;
            Response response;
            try {
                //----------------------------- buddha -----------------------------------------//
                client = new OkHttpClient();
                url = this.context.getResources().getString(R.string.url_data_download);
                request = new Request.Builder().url(url).build();

                response = client.newCall(request).execute();
//                //Log.d("CalendarThaiSyncDataService", "onHandleIntent Download Data response isSuccessful :" + response.isSuccessful());
                if (response.isSuccessful()) {
                    zis = new ZipInputStream(new BufferedInputStream(response.body().byteStream()));
                    try {
                        ZipEntry ze;
                        while ((ze = zis.getNextEntry()) != null) {
                            String filename = ze.getName();
//                            //Log.d("CalendarThaiSyncDataService", "onHandleIntent Extract filename :" + filename + "=="+this.getResources().getString(R.string.file_extract_buddhamoonphase));
                            if (filename.equals(this.context.getResources().getString(R.string.file_extract_buddhamoonphase))) {
                                //Log.d("CalendarThaiSyncDataService", "onHandleIntent Begin load BuddhaMoonPhaseDatabase:" + filename);
                                insertBuddhMoonPhase(this.context, zis);
                            } else if (filename.equals(this.context.getResources().getString(R.string.file_extract_holiday))) {
                                //Log.d("CalendarThaiSyncDataService", "onHandleIntent Begin load HolidayDatabase:" + filename);
                                insertHoliday(this.context, zis);
                            }
                        }
                    } finally {
                        zis.close();
                    }
                }
                response.body().close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                zis = null;
                client = null;
                url = null;
                request = null;
                response = null;
            }
        }
    }
}
