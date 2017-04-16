package com.oleeb.calendarthai.holiday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

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
        builder.setTables(Holiday.TB_NAME);

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
        public static final int DB_VERSION = 1;

        private Context mContext;

        public HolidayHelper(Context context) {
            super(context, Holiday.DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Holiday.SQL_CREATE);
            //Log.d(TAG, "onCreate TABLE " + Holiday.TB_NAME);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Holiday.TB_NAME);
            //Log.d(TAG, "onUpgrade DROP TABLE IF EXISTS " + Holiday.TB_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Holiday.TB_NAME);
            //Log.d(TAG, "onDowngrade DROP TABLE IF EXISTS " + Holiday.TB_NAME);
            onCreate(db);
        }

        public long insertHoliday(String locale, String date, String desc, String remark){
            ContentValues cv = new ContentValues();
            cv.put(Holiday.COL_LOCALE, locale);
            cv.put(Holiday.COL_DATE, date);
            cv.put(Holiday.COL_DESC, desc);
            cv.put(Holiday.COL_TYPE, remark);
            return getWritableDatabase().insert(Holiday.TB_NAME, null, cv);
        }
    }
}
