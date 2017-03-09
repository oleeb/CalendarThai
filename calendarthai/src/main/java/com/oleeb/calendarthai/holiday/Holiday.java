package com.oleeb.calendarthai.holiday;

/**
 * Created by j1tth4 on 6/14/14 AD.
 */
public class Holiday {
    public static final String DB_NAME = "DB_Holiday";
    public static final String TB_NAME = "Holiday";
    public static final String COL_LOCALE = "locale";
    public static final String COL_DATE = "date";
    public static final String COL_DESC = "desc";
    public static final String COL_TYPE = "type";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TB_NAME + " ("
                    + COL_LOCALE + " TEXT NOT NULL, "
                    + COL_DATE + " TEXT NOT NULL, "
                    + COL_DESC + " TEXT,"
                    + COL_TYPE + " TEXT, "
                    + " PRIMARY KEY(" + COL_LOCALE + ", " + COL_DATE + "));";
}
