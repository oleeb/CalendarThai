package com.oleeb.calendarthai.buddhamoonphase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by OLEEB on 6/12/2014.
 */
public class CalculateBuddhaMoonPhase {
    private static int sCurentBaseYear  = 0;
    private static int sPhaseOfYaer  = 0;

    private static final String SWAXING = "WX";
    private static final String SWANING = "WN";
    private static final int SDAY30 = 30;
    private static final int SDAY29 = 29;

    private static Calendar sCBeginDate = Calendar.getInstance(Locale.US);
    private static Calendar sCCurentDate = Calendar.getInstance(Locale.US);
    private static Calendar sCEndDate = Calendar.getInstance(Locale.US);

    //sCalPhaseOfYear (1 = ปีปกติ, 2 = ปีปกติ อธิวาน, 3 = ปีอธิมาส)
    //sData[0]= (WX=Waxing,ข้างขึ้นม, WN=Waning = ข้างแรม)
    //sData[1] = วันข้างขึ้น วันข้างแรม
    //sData[2] = เดือน
    //sData[3] = เดือน
    //sData[4] = false, true=วันพระ)

    @SuppressLint("LongLogTag")
    public Object[] getCalMoonPhase(Context context, int year, int month, int day){
//        //Log.d("CalculateBuddhaMoonPhase", "getBuddhaMoonPhaseDB() int year, int month, int day=" + year + ", " + month + ", " + day);
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCBeginDate:"+sCBeginDate.getTime());
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCEndDate:"+sCEndDate.getTime());
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCCurentDate:"+sCCurentDate.getTime());
        Object[] data = null;
        int countDay = 0;
        boolean hasData = false;
        sCCurentDate.set(year, month, day, 0, 0, 0);

        if (sCBeginDate.before(sCCurentDate) && sCCurentDate.before(sCEndDate)) {
//            //Log.d("CalculateBuddhaMoonPhase","---------------------------------"+year);
        }else if(sCBeginDate.before(sCCurentDate) && sCEndDate.before(sCCurentDate)){
//            //Log.d("CalculateBuddhaMoonPhase","+++++++++++++++++++++++++++++++++"+year);
            getBuddhaMoonPhaseDB(context, ++year);
        }else{
//            //Log.d("CalculateBuddhaMoonPhase","*********************************"+year);
            getBuddhaMoonPhaseDB(context, year);
        }

        if(sCurentBaseYear > 0) {
            countDay = getDaysBetween(sCBeginDate, sCCurentDate);
            data = calPhaseofYear(countDay);
        }
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCBeginDate:"+sCBeginDate.getTime());
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCEndDate:"+sCEndDate.getTime());
//        //Log.d("CalculateBuddhaMoonPhase","getCalMoonPhase() sCCurentDate:"+sCCurentDate.getTime());
//        //Log.d("CalculateBuddhaMoonPhase countDay:",""+countDay);
//        //Log.d("CalculateBuddhaMoonPhase data:",data[1]+","+data[3]);
        return data;
    }

    @SuppressLint("LongLogTag")
    private void getBuddhaMoonPhaseDB(Context context,int year){
//        //Log.d("CalculateBuddhaMoonPhase","getBuddhaMoonPhaseDB() year="+year);
//        sCurentBaseYear = 2014;
//        sPhaseOfYaer = 1;
//        sCBeginDate.set(2013, 11, 3);
//        sCEndDate.set(2014, 10, 22);
        Cursor cursor = context.getContentResolver().query(BuddhaMoonPhaseProvider.CONTENT_URI,
                new String[]{
                        BuddhaMoonPhase.COL_BASEYAER,
                        BuddhaMoonPhase.COL_BEGINDATE,
                        BuddhaMoonPhase.COL_ENDDATE,
                        BuddhaMoonPhase.COL_PHASEYEAR
                },
                BuddhaMoonPhase.COL_BASEYAER + "=?",
                new String[]{
                       String.valueOf(year)
                },
                null);
        if(cursor != null && cursor.moveToFirst()){
            sCurentBaseYear = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BuddhaMoonPhase.COL_BASEYAER)));
            String[] strBegindate = cursor.getString(cursor.getColumnIndex(BuddhaMoonPhase.COL_BEGINDATE)).split("-");
            sCBeginDate.set(Integer.parseInt(strBegindate[0]),Integer.parseInt(strBegindate[1])-1,Integer.parseInt(strBegindate[2]),0,0,0);
            String[] strEnddate = cursor.getString(cursor.getColumnIndex(BuddhaMoonPhase.COL_ENDDATE)).split("-");
            sCEndDate.set(Integer.parseInt(strEnddate[0]),Integer.parseInt(strEnddate[1])-1,Integer.parseInt(strEnddate[2]),0,0,0);
            sPhaseOfYaer = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BuddhaMoonPhase.COL_PHASEYEAR)));
        }
        if(cursor != null)cursor.close();
    }

    private int getDaysBetween(Calendar sCBeginDate, Calendar sCCurentDate){
        int countDayBetween = 0;
        if(sCBeginDate.get(Calendar.YEAR) == sCCurentDate.get(Calendar.YEAR)) {
            countDayBetween = sCCurentDate.get(Calendar.DAY_OF_YEAR) - sCBeginDate.get(Calendar.DAY_OF_YEAR);
        }else if(sCBeginDate.get(Calendar.YEAR) < sCCurentDate.get(Calendar.YEAR)){
            int dayOfyear = 0;
            while (sCBeginDate.get(Calendar.YEAR) < sCCurentDate.get(Calendar.YEAR)){
                dayOfyear +=sCCurentDate.get(Calendar.DAY_OF_YEAR);
                sCCurentDate.add(Calendar.YEAR,-1);
            }
            countDayBetween  = (sCBeginDate.getActualMaximum(Calendar.DAY_OF_YEAR) - sCBeginDate.get(Calendar.DAY_OF_YEAR)) + dayOfyear;
        }
        return ++countDayBetween;
    }

    private Object[] calPhaseofYear(int countDays){
        Object[] sData = new Object[5];
        int lunarMonth = 12;
        if(sPhaseOfYaer == 3) ++lunarMonth;
        int i = 0;
        int month = 0;
        for(;i < lunarMonth; i++){
            ++month;
            if(month%2 == 0 || ((sPhaseOfYaer == 2 && i == 6) || (sPhaseOfYaer == 3 && i == 8))){
                if((sPhaseOfYaer == 3 && i==8)) --month;
                if(countDays>SDAY30) {
                    countDays -= SDAY30;
                }else{
                    if(countDays > 15){
                        int waning = countDays-15;
                        sData[0] = SWANING;
                        sData[1] = waning;
                        sData[2] = month;
                        sData[3] = ((sPhaseOfYaer == 3 && i==8))?String.valueOf(month)+"-"+String.valueOf(month):String.valueOf(month);
                        if(waning == 8 || waning == 15) {
                            sData[4] = true;
                        }else{
                            sData[4] = false;
                        }
                    }else{
                        sData[0] = SWAXING;
                        sData[1] = countDays;
                        sData[2] = month;
                        sData[3] = ((sPhaseOfYaer == 3 && i==8))?String.valueOf(month)+"-"+String.valueOf(month):String.valueOf(month);
                        if(countDays == 8 || countDays == 15) {
                            sData[4] = true;
                        }else{
                            sData[4] = false;
                        }
                    }
                    break;
                }
            }else{
                if(countDays>SDAY29) {
                    countDays -= SDAY29;
                }else{
                    if(countDays > 15){
                        int waning = countDays-15;
                        sData[0] = SWANING;
                        sData[1] = waning;
                        sData[2] = month;
                        sData[3] = String.valueOf(month);
                        if(waning == 8 || waning == 14) {
                            sData[4] = true;
                        }else{
                            sData[4] = false;
                        }
                    }else{
                        sData[0] = SWAXING;
                        sData[1] = countDays;
                        sData[2] = month;
                        sData[3] = String.valueOf(month);
                        if(countDays == 8 || countDays == 15) {
                            sData[4] = true;
                        }else{
                            sData[4] = false;
                        }
                    }
                    break;
                }
            }
        }
        return sData;
    }
}
