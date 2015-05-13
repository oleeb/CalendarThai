package com.oleeb.calendarthai.holiday;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.oleeb.calendarthai.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by j1tth4 on 6/14/14 AD.
 */
public class HolidayDatabase {
    public static final String TAG = HolidayProvider.class.getSimpleName();
    HolidayHelper mHolidayHelper;

    public HolidayDatabase(Context context){
        mHolidayHelper = new HolidayHelper(context);
    }

    public Cursor query(String selection, String[] selectionArgs, String[] columns){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Holiday.TABLE_HOLIDAY);

        Cursor cursor = builder.query(mHolidayHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public static class HolidayHelper extends SQLiteOpenHelper {
        public static final String DB_NAME = "data_holiday";
        public static final int DB_VERSION = 1;

        private Context mContext;

        public HolidayHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Holiday.SQL_CREATE);
            loadHoliday();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Holiday.TABLE_HOLIDAY);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Holiday.TABLE_HOLIDAY);
            onCreate(db);
        }

        private void loadHoliday(){
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        loadHolidayDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void loadHolidayDate() throws IOException{
            Log.d(TAG, "Loading data holiday.");
            final Resources resources = mContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.data_holiday);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, ",");
                    long id = addHoliday(strings[0].trim(), strings[1].trim(),
                            strings[2].trim(), strings[3].trim());
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "Done loading data holiday.");
        }

        public long addHoliday(String locale, String date, String desc, String remark){
            ContentValues cv = new ContentValues();
            cv.put(Holiday.COL_LOCALE, locale);
            cv.put(Holiday.COL_DATE, date);
            cv.put(Holiday.COL_DESC, desc);
            cv.put(Holiday.COL_REMARK, remark);
            return getWritableDatabase().insert(Holiday.TABLE_HOLIDAY, null, cv);
        }
    }
}
