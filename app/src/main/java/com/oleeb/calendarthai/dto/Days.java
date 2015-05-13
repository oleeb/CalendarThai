package com.oleeb.calendarthai.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class Days {
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int TO_DAY = 3;
    public static final int TO_MONTH = 4;

    public static final int WAXING = 5;
    public static final int WAXING_DAY = 6;
    public static final int WAXING_MONTH_1 = 7;
    public static final int WAXING_MONTH_2 = 8;
    public static final int WAXING_WANPRA = 9;

    public static final int HOLIDAY = 10;

    public Map<Object, Object> data = new HashMap<Object, Object>();
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
