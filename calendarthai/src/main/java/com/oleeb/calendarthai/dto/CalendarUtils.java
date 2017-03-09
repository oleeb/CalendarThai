package com.oleeb.calendarthai.dto;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by OLEEB on 6/10/2014.
 */
public class CalendarUtils {
    public static final String[] DAYNAMESTH = {"อา","จ","อ","พ","พฤ","ศ","ส"};
    public static final String[] DAYNAMESFULLTH = {"อาทิตย์","จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"};
    public static final String[] MONTHNAMEFULLTH = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
    public static final String[] MONTHNAMETH2 = {"มกรา","กุมภา","มีนา","เมษา","พฤษภา","มิถุนา","กรกฎา","สิงหา","กันยา","ตุลา","พฤศจิกา","ธันวา"};
    public static final String[] MONTHNAMETH = {"ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."};

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

    public static int getYearEn(int year){
        return year-543;
    }
}
