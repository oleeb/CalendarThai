package com.oleeb.calendarthai.dao;

import android.content.Context;
import android.os.Bundle;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dto.CalendarUtils;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class CalendarOfMonthDao {
    private CalendarOfMonthDto mCalendarOfMonth;
    private Calendar mCalendar;

    public CalendarOfMonthDao(Calendar calendar) {
        mCalendar = calendar;
        mCalendarOfMonth = new CalendarOfMonthDto();
        mCalendarOfMonth.setYear(mCalendar.get(Calendar.YEAR));
        mCalendarOfMonth.setMonth(mCalendar.get(Calendar.MONTH));
        mCalendarOfMonth.setMonthTitle(String.valueOf(CalendarUtils.MONTHNAMEFULLTH[mCalendar.get(Calendar.MONTH)]));
        mCalendarOfMonth.setYearTitle(String.valueOf(CalendarUtils.getYearTh(mCalendar.get(Calendar.YEAR))));
    }

    public CalendarOfMonthDto getCalendar() {
        return mCalendarOfMonth;
    }
}
