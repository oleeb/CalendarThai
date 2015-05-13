package com.oleeb.calendarthai.buddhamoonphase;

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
        builder.setTables(BuddhaMoonPhase.TABLE_BUDDHAMOONPHASE);

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
        public static final String DB_NAME = "DB_Buddhamoonphase";
        public static final int DB_VERSION = 1;

        private Context mContext;

        public BuddhaMoonPhaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BuddhaMoonPhase.SQL_CREATE);
            loadBuddhMoonPhase();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BuddhaMoonPhase.TABLE_BUDDHAMOONPHASE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BuddhaMoonPhase.TABLE_BUDDHAMOONPHASE);
            onCreate(db);
        }

        private void loadBuddhMoonPhase() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadBuddhMoonPhaseDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void loadBuddhMoonPhaseDate() throws IOException {
            Log.d(TAG, "Loading data buddhamoonphase.");
            final Resources resources = mContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.data_buddhamoonphase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, ",");
                    long id = addBuddhMoonPhase(strings[0].trim(), strings[1].trim(),
                            strings[2].trim(), strings[3].trim(), strings[4].trim());
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "Done loading data buddhamoonphase.");
        }

        public long addBuddhMoonPhase(String baseyear, String begindate, String enddate, String phaseyear, String remark) {
            ContentValues cv = new ContentValues();
            cv.put(BuddhaMoonPhase.COL_BASEYAER, baseyear);
            cv.put(BuddhaMoonPhase.COL_BEGINDATE, begindate);
            cv.put(BuddhaMoonPhase.COL_ENDDATE, enddate);
            cv.put(BuddhaMoonPhase.COL_PHASEYEAR, phaseyear);
            cv.put(BuddhaMoonPhase.COL_REMARK, remark);
            return getWritableDatabase().insert(BuddhaMoonPhase.TABLE_BUDDHAMOONPHASE, null, cv);
        }
    }
}
