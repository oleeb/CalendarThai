package com.oleeb.calendarthai.buddhamoonphase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by OLEEB on 6/17/14 AD.
 */
public class BuddhaMoonPhaseDatabase {
    public static final String TAG = BuddhaMoonPhaseDatabase.class.getSimpleName();
    BuddhaMoonPhaseHelper mBuddhMoonPhaseHelper;

    public BuddhaMoonPhaseDatabase(Context context){
        mBuddhMoonPhaseHelper = new BuddhaMoonPhaseHelper(context);
    }

    public Cursor query(String selection, String[] selectionArgs, String[] columns){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(BuddhaMoonPhase.TB_NAME);

        Cursor cursor = builder.query(mBuddhMoonPhaseHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public static class BuddhaMoonPhaseHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 1;

        private Context mContext;

        public BuddhaMoonPhaseHelper(Context context) {
            super(context, BuddhaMoonPhase.DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BuddhaMoonPhase.SQL_CREATE);
            //Log.d(TAG, "onCreate TABLE " + BuddhaMoonPhase.TB_NAME);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BuddhaMoonPhase.TB_NAME);
            //Log.d(TAG, "onUpgrade DROP TABLE IF EXISTS " + BuddhaMoonPhase.TB_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BuddhaMoonPhase.TB_NAME);
            //Log.d(TAG, "onDowngrade DROP TABLE IF EXISTS " + BuddhaMoonPhase.TB_NAME);
            onCreate(db);
        }

        public long insertBuddhMoonPhase(String baseyear, String begindate, String enddate, String phaseyear, String remark) {
            ContentValues cv = new ContentValues();
            cv.put(BuddhaMoonPhase.COL_BASEYAER, baseyear);
            cv.put(BuddhaMoonPhase.COL_BEGINDATE, begindate);
            cv.put(BuddhaMoonPhase.COL_ENDDATE, enddate);
            cv.put(BuddhaMoonPhase.COL_PHASEYEAR, phaseyear);
            cv.put(BuddhaMoonPhase.COL_REMARK, remark);
            return getWritableDatabase().insert(BuddhaMoonPhase.TB_NAME, null, cv);
        }
    }
}
