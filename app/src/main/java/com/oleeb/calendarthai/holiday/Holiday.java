package com.oleeb.calendarthai.holiday;

/**
 * Created by j1tth4 on 6/14/14 AD.
 */
public class Holiday {
    public static final String TABLE_HOLIDAY = "ThaiHoliday";
    public static final String COL_LOCALE = "locale";
    public static final String COL_DATE = "date";
    public static final String COL_DESC = "desc";
    public static final String COL_REMARK = "remark";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_HOLIDAY + " ("
                    + COL_LOCALE + " TEXT NOT NULL, "
                    + COL_DATE + " TEXT NOT NULL, "
                    + COL_DESC + " TEXT,"
                    + COL_REMARK + " TEXT, "
                    + " PRIMARY KEY(" + COL_LOCALE + ", " + COL_DATE + "));";
}
