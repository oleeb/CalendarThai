package com.oleeb.calendarthai.buddhamoonphase;

/**
 * Created by OLEEB on 6/17/14 AD.
 */
public class BuddhaMoonPhase {
    public static final String DB_NAME = "DB_Buddhamoonphase";
    public static final String TB_NAME = "BuddhaMoonphase";
    public static final String COL_BASEYAER = "baseyear";
    public static final String COL_BEGINDATE = "startdate";
    public static final String COL_ENDDATE = "enddate";
    public static final String COL_PHASEYEAR = "phaseyear";
    public static final String COL_REMARK = "remark";

    public static final String SQL_CREATE =
        "CREATE TABLE " + TB_NAME + " ("
            + COL_BASEYAER + " INT NOT NULL, "
            + COL_BEGINDATE + " TEXT NOT NULL, "
            + COL_ENDDATE + " TEXT NOT NULL, "
            + COL_PHASEYEAR + " INT, "
            + COL_REMARK + " TEXT, "
            + " PRIMARY KEY(" + COL_BASEYAER + "));";
}
