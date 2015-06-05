package com.oleeb.calendarthai.dao;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.view.AbstractDaysAdapter;
import com.oleeb.calendarthai.buddhamoonphase.CalculateBuddhaMoonPhase;
import com.oleeb.calendarthai.dto.Days;
import com.oleeb.calendarthai.holiday.Holiday;
import com.oleeb.calendarthai.holiday.HolidayProvider;

import java.text.SimpleDateFormat;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class DataOfDayDao extends AbstractDaysAdapter {
    private Days days;
    private CalculateBuddhaMoonPhase calculateBuddhaMoonPhase;
    public DataOfDayDao(Context context){
        super(context);
    }

    @Override
    public Bundle getData(Days days) {
        this.days = days;
        setBuddhaMoonPhase();
        setHoliday();
        return this.days.data;
    }

    private void setBuddhaMoonPhase(){
        //Set Buddha Moon Phase
        this.calculateBuddhaMoonPhase = new CalculateBuddhaMoonPhase();
        Object[] buddhaMP = this.calculateBuddhaMoonPhase.getCalMoonPhase(getContext(),
                Integer.parseInt(new SimpleDateFormat("yyyy").format(days.getTime()).toString()),
                Integer.parseInt(new SimpleDateFormat("M").format(days.getTime()).toString())-1,
                Integer.parseInt(new SimpleDateFormat("d").format(days.getTime()).toString()));

        /***********Buddha Moon Phase Day***************/
        if(buddhaMP != null) {
            this.days.data.putString(CalendarThaiAction.WAXING, (String) buddhaMP[0]);
            this.days.data.putInt(CalendarThaiAction.WAXING_DAY, (Integer) buddhaMP[1]);
            this.days.data.putInt(CalendarThaiAction.WAXING_MONTH_1, (Integer)buddhaMP[2]);
            this.days.data.putString(CalendarThaiAction.WAXING_MONTH_2, (String)buddhaMP[3]);
            this.days.data.putBoolean(CalendarThaiAction.WAXING_WANPRA, (Boolean) buddhaMP[4]);
        }
    }

    private void setHoliday(){
        Cursor cursor = getContext().getContentResolver().query(HolidayProvider.CONTENT_URI,
                new String[]{
                        Holiday.COL_DESC,
                        Holiday.COL_REMARK
                },
                Holiday.COL_LOCALE + "=?"
                        + " AND " + Holiday.COL_DATE + "=?",
                new String[]{
                        "th", new SimpleDateFormat("yyyy-MM-dd").format(this.days.getTime()).toString()
                }, null);
        if(cursor != null){
            if(cursor.moveToFirst()) {
                this.days.data.putString(CalendarThaiAction.HOLIDAY, cursor.getString(cursor.getColumnIndex(Holiday.COL_DESC)));
            }
            cursor.close();
        }
    }
}
