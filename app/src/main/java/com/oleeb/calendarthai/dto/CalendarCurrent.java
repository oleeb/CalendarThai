package com.oleeb.calendarthai.dto;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by OLEEB on 6/10/2014.
 */
public class CalendarCurrent {
    public static final String[] DAYNAMESTH = {"อา","จ","อ","พ","พฤ","ศ","ส"};
    public static final String[] DAYNAMESFULLTH = {"อาทิตย์","จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"};
    public static final String[] MONTHNAMETH = {"มกราคม","กุมพาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};

    public static int getCurrentDay() {
        return Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_YEAR);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH);
    }

    public static int getYearTh(int year){
        return 543+year;
    }

}
