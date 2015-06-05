package com.oleeb.calendarthai.dao;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dto.CalendarCurrent;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.DaysOfWeekDto;
import com.oleeb.calendarthai.dto.WeeksOfMonthDto;
import com.oleeb.calendarthai.holiday.Holiday;
import com.oleeb.calendarthai.holiday.HolidayProvider;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class CalendarOfMonthDao {
    private CalendarOfMonthDto mCalendarOfMonth;
    private Calendar mCalendar;
    private Context mContext;

    public CalendarOfMonthDao(Context context) {
        mContext = context;
    }

    public CalendarOfMonthDto getCalendar(Calendar calendar) {
        mCalendar = calendar;
        mCalendarOfMonth = new CalendarOfMonthDto();
        mCalendarOfMonth.setYear(mCalendar.get(Calendar.YEAR));
        mCalendarOfMonth.setMonth(mCalendar.get(Calendar.MONTH));
        mCalendarOfMonth.setMonthTitle(String.valueOf(CalendarCurrent.MONTHNAMETH[mCalendar.get(Calendar.MONTH)]));
        mCalendarOfMonth.setYearTitle(String.valueOf(CalendarCurrent.getYearTh(mCalendar.get(Calendar.YEAR))));
        mCalendarOfMonth.setDayNames(getDayNames());
        mCalendarOfMonth.setWeeksOfMonthDto(getDays());
        return mCalendarOfMonth;
    }

    private String[] getDayNames(){
        String[] dayNameSymbols = new DateFormatSymbols().getShortWeekdays();
        String[] dayNames = new String[CalendarOfMonthDto.DAYS_PER_WEEK];
        for(int i = 0; i < CalendarOfMonthDto.DAYS_PER_WEEK; i++){
            dayNames[i] = CalendarCurrent.DAYNAMESTH[i];
        }
        return dayNames;
    }

    public String[] getHolidayOfMonth(){
        String[] holidays = {};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mCalendarOfMonth.getYear());
        calendar.set(Calendar.MONTH, mCalendarOfMonth.getMonth());
        Calendar dateFrom = (Calendar) calendar.clone();
        Calendar dateTo = (Calendar) calendar.clone();
        dateFrom.set(Calendar.DAY_OF_MONTH, 1);
        dateTo.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Cursor cursor = mContext.getContentResolver().query(HolidayProvider.CONTENT_URI,
                new String[]{
                        Holiday.COL_DATE,
                        Holiday.COL_DESC,
                        Holiday.COL_REMARK
                },
                Holiday.COL_LOCALE + "=?"
                        + " AND " + Holiday.COL_DATE + " BETWEEN ? AND ? ",
                new String[]{
                        "th",
                        new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getTime()).toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getTime()).toString()
                }, null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                holidays = new String[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++) {
                    SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
                    String holidayDate = "";
                    try {
                        Date date = sp.parse(
                                cursor.getString(cursor.getColumnIndex(Holiday.COL_DATE)));
                        sp.applyPattern("d");
                        holidayDate = sp.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holidays[i] = holidayDate
                            + " " + cursor.getString(cursor.getColumnIndex(Holiday.COL_DESC));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return holidays;
    }

    private WeeksOfMonthDto getDays(){
        //-----------Days of Month---------------
        int thisMonth = mCalendarOfMonth.getMonth();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int numWeeks = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int todayDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        mCalendar.add(Calendar.DAY_OF_MONTH, 1 - todayDayOfWeek);

        WeeksOfMonthDto weeksOfMonthDto = new WeeksOfMonthDto();
        DaysOfWeekDto daysOfWeekDto = null;
        Days days = null;
        for (int week = 0; week < numWeeks; week++) {
            daysOfWeekDto = new DaysOfWeekDto();
            for (int day = 0; day < CalendarOfMonthDto.DAYS_PER_WEEK; day++) {
                boolean inMonth = mCalendar.get(Calendar.MONTH) == thisMonth;
                boolean isToday = (mCalendar.get(Calendar.YEAR) == CalendarCurrent.getCurrentYear())
                        && (mCalendar.get(Calendar.MONTH) == CalendarCurrent.getCurrentMonth())
                        && (mCalendar.get(Calendar.DAY_OF_YEAR) == CalendarCurrent.getCurrentDay());
                days = new Days();
                days.setTime(mCalendar.getTimeInMillis());
                days.data.putInt(CalendarThaiAction.YEAR, mCalendar.get(Calendar.YEAR));
                days.data.putInt(CalendarThaiAction.MONTH, mCalendar.get(Calendar.MONTH));
                days.data.putInt(CalendarThaiAction.DAY, mCalendar.get(Calendar.DAY_OF_MONTH));

                days.data.putInt(CalendarThaiAction.DAY_IN_WEEK, day);

                if(isToday) {
                    days.data.putBoolean(CalendarThaiAction.TO_DAY, true); // to day
                }else{
                    days.data.putBoolean(CalendarThaiAction.TO_DAY, false);
                }

                if(inMonth){
                    days.data.putBoolean(CalendarThaiAction.TO_MONTH, true); // in month
                }else{
                    days.data.putBoolean(CalendarThaiAction.TO_MONTH, false); // other
                }

                DataOfDayDao daysAdapter = new DataOfDayDao(mContext);
                Bundle mapDays = daysAdapter.getData(days);

                if(mapDays != null){
                    days.data.putAll(mapDays);
                }

                daysOfWeekDto.daysOfWeek.add(days);
                mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }// End Day
            weeksOfMonthDto.weeksOfMonth.add(daysOfWeekDto);
        }// End Week
        return weeksOfMonthDto;
    }
}
