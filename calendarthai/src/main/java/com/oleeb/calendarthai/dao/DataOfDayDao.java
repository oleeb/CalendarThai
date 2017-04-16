package com.oleeb.calendarthai.dao;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.buddhamoonphase.BuddhaMoonPhase;
import com.oleeb.calendarthai.buddhamoonphase.BuddhaMoonPhaseProvider;
import com.oleeb.calendarthai.buddhamoonphase.CalculateBuddhaMoonPhase;
import com.oleeb.calendarthai.dto.DataOfDayDto;
import com.oleeb.calendarthai.holiday.Holiday;
import com.oleeb.calendarthai.holiday.HolidayProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class DataOfDayDao {
    private Context context;
    private Bundle data;
    private Calendar cal;
    private CalculateBuddhaMoonPhase calculateBuddhaMoonPhase;
    DataOfDayDto dataOfDayDto;
    public DataOfDayDao(Context context){
        this.context = context;
    }
    public DataOfDayDao(Context context, Calendar cal){
        this.context = context;
        this.cal = cal;
        this.data = new Bundle();
        this.data.putInt(CalendarThaiAction.YEAR, this.cal.get(Calendar.YEAR));
        this.data.putInt(CalendarThaiAction.MONTH, this.cal.get(Calendar.MONTH));
        this.data.putInt(CalendarThaiAction.DAY, this.cal.get(Calendar.DAY_OF_MONTH));
        this.data.putInt(CalendarThaiAction.DAY_OF_WEEK, this.cal.get(Calendar.DAY_OF_WEEK));
        this.dataOfDayDto = new DataOfDayDto();
        setBuddhaMoonPhase();
        setHoliday();
    }

    public DataOfDayDto getData() {
        dataOfDayDto.setData(this.data);
        return dataOfDayDto;
    }

    public boolean chkDB(){
        boolean chkDB = false;
        Cursor cursor = this.context.getContentResolver().query(BuddhaMoonPhaseProvider.CONTENT_URI,
                new String[]{
                        BuddhaMoonPhase.COL_BASEYAER,
                        BuddhaMoonPhase.COL_BEGINDATE,
                        BuddhaMoonPhase.COL_ENDDATE,
                        BuddhaMoonPhase.COL_PHASEYEAR
                },
                BuddhaMoonPhase.COL_BASEYAER + "=?",
                new String[]{
                        "1759"
                },
                null);
        if(cursor != null){
            chkDB = true;
            cursor.close();
        }
        //Log.d("chkDB",""+chkDB);
        return chkDB;
    }
    private void setBuddhaMoonPhase(){
        //Set Buddha Moon Phase
        this.calculateBuddhaMoonPhase = new CalculateBuddhaMoonPhase();
        Object[] buddhaMP = this.calculateBuddhaMoonPhase.getCalMoonPhase(this.context,
                Integer.parseInt(new SimpleDateFormat("yyyy").format(this.cal.getTimeInMillis()).toString()),
                Integer.parseInt(new SimpleDateFormat("M").format(this.cal.getTimeInMillis()).toString())-1,
                Integer.parseInt(new SimpleDateFormat("d").format(this.cal.getTimeInMillis()).toString()));

        /***********Buddha Moon Phase Day***************/
        if(buddhaMP != null && buddhaMP.length > 0) {
            this.data.putString(CalendarThaiAction.WAXING, buddhaMP[0] != null ? (String) buddhaMP[0] : "");
            this.data.putString(CalendarThaiAction.WAXING_DAY, buddhaMP[1] != null ? (String) String.valueOf(buddhaMP[1]) : "");
            this.data.putString(CalendarThaiAction.WAXING_MONTH_1, buddhaMP[2] != null ? (String) String.valueOf(buddhaMP[2]) : "");
            this.data.putString(CalendarThaiAction.WAXING_MONTH_2, buddhaMP[3] != null ? (String) String.valueOf(buddhaMP[3]) : "");
            this.data.putBoolean(CalendarThaiAction.WAXING_WANPRA, buddhaMP[4] != null ? (Boolean) buddhaMP[4] : false);
        }else{
            this.data.putString(CalendarThaiAction.WAXING, "");
            this.data.putString(CalendarThaiAction.WAXING_DAY, "");
            this.data.putString(CalendarThaiAction.WAXING_MONTH_1, "");
            this.data.putString(CalendarThaiAction.WAXING_MONTH_2, "");
            this.data.putBoolean(CalendarThaiAction.WAXING_WANPRA, false);
        }
    }

    private void setHoliday(){
        Cursor cursor = this.context.getContentResolver().query(HolidayProvider.CONTENT_URI,
                new String[]{
                        Holiday.COL_DESC,
                        Holiday.COL_TYPE
                },
                Holiday.COL_LOCALE + "=?"
                        + " AND " + Holiday.COL_DATE + "=?",
                new String[]{
                        "th", new SimpleDateFormat("yyyy-MM-dd").format(this.cal.getTimeInMillis()).toString()
                }, null);
        if(cursor != null){
            if(cursor.moveToFirst()) {
                this.data.putString(CalendarThaiAction.IMPORTANT_DESC, cursor.getString(cursor.getColumnIndex(Holiday.COL_DESC)));
                this.data.putInt(CalendarThaiAction.IMPORTANT_TYPE, cursor.getInt(cursor.getColumnIndex(Holiday.COL_TYPE)));
            }
        }
        if(cursor != null)cursor.close();
    }
}
