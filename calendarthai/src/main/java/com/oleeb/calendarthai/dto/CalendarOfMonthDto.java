package com.oleeb.calendarthai.dto;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class CalendarOfMonthDto {
    public static final int DAYS_PER_WEEK = 7;
    private String yearTitle;
    private String monthTitle;
    private int year;
    private int month;

    public String getYearTitle() {
        return yearTitle;
    }

    public void setYearTitle(String yearTitle) {
        this.yearTitle = yearTitle;
    }

    public String getMonthTitle() {
        return monthTitle;
    }

    public void setMonthTitle(String monthTitle) {
        this.monthTitle = monthTitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
