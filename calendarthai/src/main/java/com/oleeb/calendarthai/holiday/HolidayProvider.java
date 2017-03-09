package com.oleeb.calendarthai.holiday;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by j1tth4 on 6/14/14 AD.
 */
public class HolidayProvider extends ContentProvider {

    /**
     * The authority
     */
    public static final String AUTHORITY = "com.oleeb.calendarthai.holiday.HolidayProvider";

    /**
     * Content Uri
     */
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + Holiday.TB_NAME);
    /**
     * UriMatcher
     */
    private static final UriMatcher sUriMatcher;

    private static final int GET_HOLIDAY = 0;

    //private static final int
    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, Holiday.TB_NAME, GET_HOLIDAY);
    }

    private HolidayDatabase mHolidayDatabase;

    @Override
    public boolean onCreate() {
        mHolidayDatabase = new HolidayDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        switch (sUriMatcher.match(uri)){
            case GET_HOLIDAY:
                return mHolidayDatabase.query(selection, selectionArgs, projection);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues,
                      String selection, String[] selectionArgs) {
        return 0;
    }
}
