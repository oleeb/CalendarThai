package com.oleeb.calendarthai.dto;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.buddhamoonphase.CalculateBuddhaMoonPhase;
import com.oleeb.calendarthai.holiday.Holiday;
import com.oleeb.calendarthai.holiday.HolidayProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class DataOfDayDto {
    private Bundle data;

    public void setData(Bundle data) {
        this.data = data;
    }
    public Bundle getData() {
        return this.data;
    }
}
