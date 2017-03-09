package com.oleeb.calendarthai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oleeb.calendarthai.action.CalendarThaiAction;
import com.oleeb.calendarthai.dao.CalendarOfMonthDao;
import com.oleeb.calendarthai.dao.DataOfDayDao;
import com.oleeb.calendarthai.dto.CalendarOfMonthDto;
import com.oleeb.calendarthai.dto.CalendarUtils;
import com.oleeb.calendarthai.dto.DataOfDayDto;
import com.oleeb.calendarthai.task.CalendarThaiAsyncTask;

import java.util.Calendar;

public class CalendarThai extends AppCompatActivity {
    private SharedPreferences sharedPrefs;
    private NestedScrollView nesScrollView;
    private LinearLayout ll_activity_calendar_thai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar_thai);

        PreferenceManager.setDefaultValues(this, R.xml.calendarthai_settings, true);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(getSupportActionBar().getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbar_transparent));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nesScrollView = (NestedScrollView)findViewById(R.id.nesScrollView);
//        nesScrollView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background_transparent));

        //begin remove content all
        nesScrollView.removeAllViews();

        ll_activity_calendar_thai = (LinearLayout) getLayoutInflater().inflate(
                R.layout.activity_calendar_thai, null);

        //set calendar background color
//        ll_activity_calendar_thai.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background_calendar));

        nesScrollView.addView(ll_activity_calendar_thai);
        drawWidgetListDay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_thai, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_action_settings:
                Intent intent = new Intent(this, CalendarThaiSettingsActivity.class);
                this.startActivity(intent);
                //this.finish();
                return true;
            case R.id.menu_action_refresh:
                CalendarThaiAsyncTask task = new CalendarThaiAsyncTask(this);
//                task.setProgressBar(progress_bar);
                task.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawWidgetListDay();
    }

    private void drawWidgetListDay() {
        Log.d("drawWidgetListDay", "");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE , 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH,
                cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR,
                cal.get(Calendar.YEAR)));

        CalendarOfMonthDao calendarOfMonthDao = new CalendarOfMonthDao(cal);
        CalendarOfMonthDto calendarOfMonthDto = calendarOfMonthDao.getCalendar();

        //begin remove content all
        ll_activity_calendar_thai.removeAllViews();

        //month bar
        LinearLayout ll_row_month_bar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_month_bar, null);
        TextView tvTitleMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvTitleMonth);
        TextView tvPrevMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvPrevMonth);
        TextView tvNextMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvNextMonth);
        TextView tvCurrMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvCurrMonth);

        tvTitleMonth.setText(calendarOfMonthDto.getMonthTitle() + " "
                + calendarOfMonthDto.getYearTitle());

        ll_activity_calendar_thai.addView(ll_row_month_bar);

        //btn control row_month_bar
        if(cal.get(Calendar.YEAR) > getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            tvPrevMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickBtnEvent(CalendarThaiAction.ACTION_PREVIOUS_MONTH, null);
                                }
                            }
                    );
        }else{
            tvPrevMonth.setVisibility(View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            tvNextMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickBtnEvent(CalendarThaiAction.ACTION_NEXT_MONTH, null);
                                }
                            }
                    );
        }else{
            tvNextMonth.setVisibility(View.INVISIBLE);
        }

        tvCurrMonth
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickBtnEvent(CalendarThaiAction.ACTION_RESET_MONTH, null);
                            }
                        }
                );

        tvTitleMonth
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickBtnEvent(CalendarThaiAction.ACTION_LIST_MONTH, null);
                            }
                        }
                );


        //days header name
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

        LinearLayout ll_row_days_header = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_days_header, null);
        for(int i = 0; i < CalendarOfMonthDto.DAYS_PER_WEEK; i++){
            LinearLayout ll_cell_days_header = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.cell_days_header, null);
            TextView tvDayName = (TextView)ll_cell_days_header.findViewById(R.id.tvDayName);
            tvDayName.setText(CalendarUtils.DAYNAMESTH[i]);
            ll_row_days_header.addView(ll_cell_days_header, params);
        }
        ll_activity_calendar_thai.addView(ll_row_days_header);

        //Days of Month
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        LinearLayout ll_row_calendar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_calendar,null);

        int thisMonth = calendarOfMonthDto.getMonth();
        int thisYear = calendarOfMonthDto.getYear();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int numWeeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        cal.add(Calendar.DAY_OF_MONTH, 1 - todayDayOfWeek);

        DataOfDayDao dataOfDayDao = null;
        DataOfDayDto dataOfDayDto = null;
        for (int week = 0; week < numWeeks; week++) {
            LinearLayout ll_row_week = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.row_week, null);
            for (int day = 0; day < CalendarOfMonthDto.DAYS_PER_WEEK; day++) {
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean isToday = (cal.get(Calendar.YEAR) == CalendarUtils.getCurrentYear())
                        && (cal.get(Calendar.MONTH) == CalendarUtils.getCurrentMonth())
                        && (cal.get(Calendar.DAY_OF_YEAR) == CalendarUtils.getCurrentDay());

                dataOfDayDao = new DataOfDayDao(getApplicationContext(), cal);
                dataOfDayDto = dataOfDayDao.getData();
                dataOfDayDto.getData().putBoolean(CalendarThaiAction.TO_DAY, isToday);
                //set views text days
                RelativeLayout rl_cell_days = (RelativeLayout) getLayoutInflater().inflate(
                        R.layout.cell_days, null);
                TextView tvDay = (TextView) rl_cell_days.findViewById(R.id.tvDay);
                tvDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                //set text color
                if (!inMonth) {// day other month
                    tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_other_month));
                } else if (day == 0){// in holiday
                    tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday));
                } else {// in month
                    tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
                }
                //sert To day Background
                if (isToday) {// to day
                    rl_cell_days.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.today_background));
//                    rv_cell_days.setInt(R.id.layCellDayContainer, "setBackgroundResource", R.drawable.shape_circle);
                }
                //set views text holidays and important day
                TextView tvHoliday = (TextView) rl_cell_days.findViewById(R.id.tvHoliday);
                if(dataOfDayDto.getData() != null  && dataOfDayDto.getData().getString(
                        CalendarThaiAction.IMPORTANT_DESC) != null
                        && !dataOfDayDto.getData().getString(
                        CalendarThaiAction.IMPORTANT_DESC).equals("")) {
                    tvHoliday.setText(dataOfDayDto.getData().getString(CalendarThaiAction.IMPORTANT_DESC));
                    tvHoliday.setVisibility(View.VISIBLE);
                    //set text color
                    if (inMonth) {
                        if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                            tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday));
                            tvHoliday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday));
                        } else if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                            tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important));
                            tvHoliday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important));
                        }
                    } else {
                        if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                            tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday_other_month));
                            tvHoliday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday_other_month));
                        } else if (dataOfDayDto.getData().getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                            tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important_other_month));
                            tvHoliday.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important_other_month));
                        }
                    }
                }
                //set views text waxing waning
                TextView tvWax = (TextView) rl_cell_days.findViewById(R.id.tvWax);
                if(dataOfDayDto.getData() != null
                        && !dataOfDayDto.getData().getString(
                        CalendarThaiAction.WAXING_DAY).equals("")) {
                    String str_wax = (dataOfDayDto.getData()
                            .getString(CalendarThaiAction.WAXING)
                            .equals(CalendarThaiAction.WAXING) ?
                            getString(R.string.txt_waxing) : getString(R.string.txt_waning))
                            + " " + dataOfDayDto.getData().getString(CalendarThaiAction.WAXING_DAY)
                            + " " + getString(R.string.txt_subfix)
                            + " " + getString(R.string.txt_month)
                            +" "+ dataOfDayDto.getData().getString(CalendarThaiAction.WAXING_MONTH_2);
                    tvWax.setText(str_wax);
                    //set text color
                    if (!inMonth) {// day other month
                        tvWax.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_other_month));
                    } else {
                        tvWax.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
                    }
                } else {
                    tvWax.setVisibility(View.VISIBLE);
                }

                //set image wanpra
                ImageView ivWanpra = (ImageView) rl_cell_days.findViewById(R.id.ivWanpra);
                if (inMonth && dataOfDayDto.getData().getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                    ivWanpra.setImageResource(R.drawable.ic_wanpra);
                    ivWanpra.setVisibility(View.VISIBLE);
                }else if(dataOfDayDto.getData().getBoolean(CalendarThaiAction.WAXING_WANPRA)){
                    ivWanpra.setImageResource(R.drawable.ic_wanpra_other);
                    ivWanpra.setVisibility(View.VISIBLE);
                }

                ll_row_week.addView(rl_cell_days, params);

                //btn control
                if (inMonth) {
                    final Bundle bundle = new Bundle();
                    bundle.putBundle(CalendarThaiAction.EXTRA_DAY_DETAIL, dataOfDayDto.getData());
                    if(isToday) {
                        drawWidgetDetailDayToolbar(bundle);
                        drawWidgetEventDetail(bundle);
                    } else if((cal.get(Calendar.YEAR) == CalendarUtils.getCurrentYear())
                            && (cal.get(Calendar.MONTH) != CalendarUtils.getCurrentMonth())
                            && (cal.get(Calendar.DAY_OF_MONTH) == 1)) {
                        drawWidgetDetailDayToolbar(bundle);
                        drawWidgetEventDetail(bundle);
                    } else if((cal.get(Calendar.YEAR) != CalendarUtils.getCurrentYear())
                            && (cal.get(Calendar.DAY_OF_MONTH) == 1)) {
                        drawWidgetDetailDayToolbar(bundle);
                        drawWidgetEventDetail(bundle);
                    }

                    rl_cell_days
                            .setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            drawWidgetDetailDayToolbar(bundle);
                                            drawWidgetEventDetail(bundle);
                                        }
                                    }
                            );
                }

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }// End Day
            ll_row_calendar.addView(ll_row_week, params);
        }// End Week
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                768, 1f);
        ll_activity_calendar_thai.addView(ll_row_calendar, params);

    }

    @SuppressLint("LongLogTag")
    private void drawWidgetDetailDayToolbar(Bundle extras) {
        Log.d("drawWidgetDetailDayToolbar", "");

        Bundle data = extras.getBundle(CalendarThaiAction.EXTRA_DAY_DETAIL);

        LinearLayout llCalendarToolbar = (LinearLayout)findViewById(R.id.llCalendarToolbar);
        llCalendarToolbar.removeAllViews();

        LinearLayout ll_cell_day_detail = (LinearLayout) getLayoutInflater().inflate(
                R.layout.cell_day_detail, null);

        if(data != null) {
            //day
            TextView tvDay = (TextView)ll_cell_day_detail.findViewById(R.id.tvDay);
            tvDay.setText(String.valueOf(data.getInt(CalendarThaiAction.DAY)));
            if(data.getBoolean(CalendarThaiAction.TO_DAY)) {
                ll_cell_day_detail.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.today_background));
            }
            if (data.getInt(CalendarThaiAction.DAY_OF_WEEK) == 1 || data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday));
//                rv_cell_day_detail.setTextColor(R.id.tvHoliday, getResources().getInteger(R.color.day_in_holiday));
            } else if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important));
//                rv_cell_day_detail.setTextColor(R.id.tvHoliday, getResources().getInteger(R.color.day_in_important));
            } else {
                tvDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
            }
            // wax
            TextView tvWax = (TextView)ll_cell_day_detail.findViewById(R.id.tvWax);
            if (data != null && !data.getString(CalendarThaiAction.WAXING_DAY).equals("")) {
                String str_wax = (data.getString(CalendarThaiAction.WAXING).equals(CalendarThaiAction.WAXING) ? getResources().getString(R.string.txt_waxing) : getResources().getString(R.string.txt_waning))
                        + " " + data.getString(CalendarThaiAction.WAXING_DAY) + " " + getResources().getString(R.string.txt_subfix)
                        + " " + getResources().getString(R.string.txt_month) + " " + data.getString(CalendarThaiAction.WAXING_MONTH_2);
                tvWax.setText(str_wax);
                tvWax.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
            } else {
                tvWax.setVisibility(View.VISIBLE);
            }
            //month year
            TextView tvTitleMonth = (TextView)ll_cell_day_detail.findViewById(R.id.tvTitleMonth);
            tvTitleMonth.setText(CalendarUtils.MONTHNAMETH[data.getInt(CalendarThaiAction.MONTH)] + " " + CalendarUtils.getYearTh(data.getInt(CalendarThaiAction.YEAR)));
            tvTitleMonth.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
            //day name
            TextView tvDayName = (TextView)ll_cell_day_detail.findViewById(R.id.tvDayName);
            tvDayName.setText(CalendarUtils.DAYNAMESFULLTH[data.getInt(CalendarThaiAction.DAY_OF_WEEK) - 1]);
            tvDayName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_month));
            //image wanpra
            ImageView ivWanpra = (ImageView)ll_cell_day_detail.findViewById(R.id.ivWanpra);
            if (data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
                ivWanpra.setBackgroundResource(R.drawable.ic_wanpra_xx);
            } else {
                ivWanpra.setVisibility(View.VISIBLE);
            }
        }
        llCalendarToolbar.addView(ll_cell_day_detail);
    }

    private void drawWidgetEventDetail(Bundle extras){
        Log.d("drawWidgetEventDetail", "Bundle :" + extras);
        Bundle data = extras.getBundle(CalendarThaiAction.EXTRA_DAY_DETAIL);
        LinearLayout ll_row_event;
        ll_row_event = (LinearLayout)findViewById(R.id.llRowEvent);
        if(ll_row_event == null) {
           ll_row_event = (LinearLayout) getLayoutInflater().inflate(
                   R.layout.row_event, null);
        }else {
            ll_row_event.removeAllViews();
        }

        if(data != null) {
            //Holiday
            if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 1) {
                LinearLayout ll_cell_event = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.cell_event, null);
//                ImageView imageView1 = (ImageView)ll_cell_event.findViewById(R.id.imageView1);
//                imageView1.setImageResource(resId[i]);
//                imageView1.setVisibility(View.VISIBLE);

                TextView textView1 = (TextView) ll_cell_event.findViewById(R.id.textView1);
                textView1.setText(data.getString(CalendarThaiAction.IMPORTANT_DESC));
                textView1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_holiday));
                textView1.setVisibility(View.VISIBLE);
                textView1.setSingleLine(false);

                ll_row_event.addView(ll_cell_event);
            }

            if (data.getInt(CalendarThaiAction.IMPORTANT_TYPE) == 2) {
                LinearLayout ll_cell_event = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.cell_event, null);
//                ImageView imageView1 = (ImageView)ll_cell_event.findViewById(R.id.imageView1);
//                imageView1.setImageResource(resId[i]);
//                imageView1.setVisibility(View.VISIBLE);

                TextView textView1 = (TextView) ll_cell_event.findViewById(R.id.textView1);
                textView1.setText(data.getString(CalendarThaiAction.IMPORTANT_DESC));
                textView1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.day_in_important));
                textView1.setVisibility(View.VISIBLE);
                textView1.setSingleLine(false);

                ll_row_event.addView(ll_cell_event);
            }
            //wanpra
//            if (data.getBoolean(CalendarThaiAction.WAXING_WANPRA)) {
//                rv_cell_event = new RemoteViews(getPackageName(),
//                        R.layout.cell_event);
//                rv_cell_event.setImageViewResource(R.id.imageView1, R.drawable.ic_wanpra_xx);
//                rv_cell_event.setViewVisibility(R.id.imageView1, View.VISIBLE);
//
//                rv_cell_event.setTextViewText(R.id.textView1,
//                        getResources().getString(R.string.txt_wanpra));
//                rv_cell_event.setViewVisibility(R.id.textView1, View.VISIBLE);
//
//                rv_row_event.addView(R.id.llRowEvent, rv_cell_event);
//                maxView--;
//            }
        }
//        String[] mWidgetItems = {
//                "Aerith Gainsborough Aerith Gainsborough Aerith Gainsborough"
//                , "Barret Wallace"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                ,"Aerith Gainsborough Aerith Gainsborough Aerith Gainsborough"
//                , "Barret Wallace"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                ,"Aerith Gainsborough Aerith Gainsborough Aerith Gainsborough"
//                , "Barret Wallace"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//                , "Barret Wallace 2"
//        };
//        int[] resId = {
//                R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                ,R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                ,R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//                , R.drawable.ic_wanpra_xx
//        };
//
//
//        for(int i=0;i<mWidgetItems.length && i<maxView;i++) {
//            LinearLayout ll_cell_event = (LinearLayout) getLayoutInflater().inflate(
//                    R.layout.cell_event, null);
//            ImageView imageView1 = (ImageView)ll_cell_event.findViewById(R.id.imageView1);
//            imageView1.setImageResource(resId[i]);
//            imageView1.setVisibility(View.VISIBLE);
//
//            TextView textView1 = (TextView)ll_cell_event.findViewById(R.id.textView1);
//            textView1.setText("19:00 " + mWidgetItems[i]);
//            textView1.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.day_in_month));
//            textView1.setVisibility(View.VISIBLE);
//
//            ll_row_event.addView(ll_cell_event);
//        }
        if((LinearLayout)findViewById(R.id.llRowEvent) == null) {
            ll_activity_calendar_thai.addView(ll_row_event);
        }
    }

    private void drawWidgetListMonth() {
        Log.d("drawWidgetListMonth", "");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        //begin remove content all
        ll_activity_calendar_thai.removeAllViews();

        //month bar
        LinearLayout ll_row_month_bar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_month_bar, null);
        TextView tvTitleMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvTitleMonth);
        TextView tvPrevMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvPrevMonth);
        TextView tvNextMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvNextMonth);
        TextView tvCurrMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvCurrMonth);

        tvTitleMonth.setText(String.valueOf(CalendarUtils.getYearTh(cal.get(Calendar.YEAR))));
        ll_activity_calendar_thai.addView(ll_row_month_bar);

        //param
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                786, 1f);

        //Days of Month
        LinearLayout ll_row_calendar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_calendar, null);

        int row_month = 4;
        int index_month = 0;
        for (int row = 0; row < row_month; row++) {
            LinearLayout ll_row_month = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.row_month, null);
            for (int col = 0; col < CalendarUtils.MONTHNAMETH.length/row_month; col++){
                LinearLayout ll_cell_month = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.cell_month, null);
                TextView tvMonthName = (TextView)ll_cell_month.findViewById(R.id.tvMonthName);
                tvMonthName.setText(String.valueOf(CalendarUtils.MONTHNAMETH[index_month]));
                if(index_month == cal.get(Calendar.MONTH)) {//to month
                    ll_cell_month.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.today_background));
                }

                //btn control
                final int index_month_select = index_month;
                ll_cell_month
                        .setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(CalendarThaiAction.EXTRA_MONTH_DETAIL, index_month_select);
                                        clickBtnEvent(CalendarThaiAction.ACTION_LIST_DAY, bundle);
                                    }
                                }
                        );

                ll_row_month.addView(ll_cell_month, params);
                index_month++;
            }// End Colume
            ll_row_calendar.addView(ll_row_month, params2);
        }// End Row

        ll_activity_calendar_thai.addView(ll_row_calendar, params2);

        //btn control row_month_bar
        if(cal.get(Calendar.YEAR) > getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            tvPrevMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickBtnEvent(CalendarThaiAction.ACTION_PREVIOUS_YEAR, null);
                                }
                            }
                    );
        }else{
            tvPrevMonth.setVisibility(View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            tvNextMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickBtnEvent(CalendarThaiAction.ACTION_NEXT_YEAR, null);
                                }
                            }
                    );
        }else{
            tvNextMonth.setVisibility(View.INVISIBLE);
        }

        tvCurrMonth
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickBtnEvent(CalendarThaiAction.ACTION_RESET_MONTH, null);
                            }
                        }
                );

        tvTitleMonth
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickBtnEvent(CalendarThaiAction.ACTION_LIST_YEAR, null);
                            }
                        }
                );
    }

    private void drawWidgetListYear() {
        Log.d("drawWidgetListYear", "");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
        cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
        cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));

        //begin remove content all
        ll_activity_calendar_thai.removeAllViews();

        //show 20 col
        final int numOfYear = 20;
        final int rowOfYear = 5;
        int colOfYear = numOfYear/rowOfYear;
        int roundOfYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR)) % numOfYear;
        int beginYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR)) - roundOfYear;
        int toYear = CalendarUtils.getYearTh(cal.get(Calendar.YEAR));

//        Log.d("colOfYear",""+colOfYear);
//        Log.d("roundOfYear",""+roundOfYear);
//        Log.d("beginYear",""+beginYear);
//        Log.d("toYear",""+toYear);

        //month bar
        LinearLayout ll_row_month_bar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_month_bar, null);
        TextView tvTitleMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvTitleMonth);
        TextView tvPrevMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvPrevMonth);
        TextView tvNextMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvNextMonth);
        TextView tvCurrMonth = (TextView) ll_row_month_bar.findViewById(R.id.tvCurrMonth);

        tvTitleMonth.setText(String.valueOf(beginYear)+" - "+String.valueOf(beginYear+numOfYear-1));
        ll_activity_calendar_thai.addView(ll_row_month_bar);

        //param
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                786, 1f);

        //Days of Month
        LinearLayout ll_row_calendar = (LinearLayout) getLayoutInflater().inflate(
                R.layout.row_calendar, null);

        for (int row = 0; row < rowOfYear; row++) {
            LinearLayout ll_row_year = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.row_year, null);
            for (int col = 0; col < colOfYear; col++){
                LinearLayout ll_cell_year = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.cell_year, null);
                TextView tvYearName = (TextView)ll_cell_year.findViewById(R.id.tvYearName);
                tvYearName.setText(String.valueOf(beginYear));
                if(beginYear == toYear) {
                    ll_cell_year.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.today_background));
                }
                //btn control
                final int yearSelect = beginYear;
                ll_cell_year
                        .setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(CalendarThaiAction.EXTRA_YEAR_DETAIL, CalendarUtils.getYearEn(yearSelect));
                                        clickBtnEvent(CalendarThaiAction.ACTION_LIST_MONTH, bundle);
                                    }
                                }
                        );

                ll_row_year.addView(ll_cell_year, params);
                beginYear++;
            }// End Colume
            ll_row_calendar.addView(ll_row_year, params2);
        }// End Row

        ll_activity_calendar_thai.addView(ll_row_calendar, params2);

        //btn control row_month_bar
        if(cal.get(Calendar.YEAR) > getResources().getInteger(R.integer.CAL_YEAR_MIN)) {
            tvPrevMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(CalendarThaiAction.NUM_YEAR_GROUP, numOfYear);
                                    clickBtnEvent(CalendarThaiAction.ACTION_PREVIOUS_YEAR_GROUP, bundle);
                                }
                            }
                    );
        }else{
            tvPrevMonth.setVisibility(View.INVISIBLE);
        }
        if(cal.get(Calendar.YEAR) < getResources().getInteger(R.integer.CAL_YEAR_MAX)) {
            tvNextMonth
                    .setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(CalendarThaiAction.NUM_YEAR_GROUP, numOfYear);
                                    clickBtnEvent(CalendarThaiAction.ACTION_NEXT_YEAR_GROUP, bundle);
                                }
                            }
                    );
        }else{
            tvNextMonth.setVisibility(View.INVISIBLE);
        }

        tvCurrMonth
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickBtnEvent(CalendarThaiAction.ACTION_RESET_MONTH, null);
                            }
                        }
                );

//        tvTitleMonth
//                .setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                clickBtnEvent(CalendarThaiAction.ACTION_LIST_YEAR, null);
//                            }
//                        }
//                );
    }
    
    @SuppressLint("LongLogTag")
    public void clickBtnEvent(String action, Bundle extras) {
        if (CalendarThaiAction.ACTION_PREVIOUS_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.MONTH, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListDay();
        } else if (CalendarThaiAction.ACTION_NEXT_MONTH.equals(action)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.MONTH, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListDay();
        } else if (CalendarThaiAction.ACTION_RESET_MONTH.equals(action)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .remove(CalendarThaiAction.PREF_DATE)
                        .remove(CalendarThaiAction.PREF_MONTH)
                        .remove(CalendarThaiAction.PREF_YEAR)
                        .commit();
            }
            drawWidgetListDay();
        } else if (CalendarThaiAction.ACTION_LIST_DAY.equals(action)) {
            if(extras != null && extras.containsKey(CalendarThaiAction.EXTRA_MONTH_DETAIL)) {
               int month = extras.getInt(CalendarThaiAction.EXTRA_MONTH_DETAIL);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                            .apply();
                } else {
                    sharedPrefs.edit()
                            .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                            .commit();
                }
            }
            drawWidgetListDay();
        } else if (CalendarThaiAction.ACTION_DETAIL_DAY.equals(action) && extras != null) {
//            drawWidgetDetailDay(context, appWidgetId, extras);
        } else if (CalendarThaiAction.ACTION_LIST_MONTH.equals(action)) {
            Log.d("ACTION_LIST_MONTH extras", "" + (extras != null && extras.containsKey(CalendarThaiAction.EXTRA_YEAR_DETAIL)));
            if(extras != null && extras.containsKey(CalendarThaiAction.EXTRA_YEAR_DETAIL)) {
                int year = extras.getInt(CalendarThaiAction.EXTRA_YEAR_DETAIL);
                Log.d("ACTION_LIST_MONTH year",""+year);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
                cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
                cal.set(Calendar.YEAR, year);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sharedPrefs
                            .edit()
                            .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                            .apply();
                } else {
                    sharedPrefs.edit()
                            .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                            .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                            .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                            .commit();
                }
            }
            drawWidgetListMonth();
        } else if (CalendarThaiAction.ACTION_PREVIOUS_YEAR.equals(action)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListMonth();
        } else if (CalendarThaiAction.ACTION_NEXT_YEAR.equals(action)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListMonth();
        } else if (CalendarThaiAction.ACTION_LIST_YEAR.equals(action)) {
            drawWidgetListYear();
        } else if (CalendarThaiAction.ACTION_PREVIOUS_YEAR_GROUP.equals(action)
                && extras != null && extras.containsKey(CalendarThaiAction.NUM_YEAR_GROUP)) {
            int numOfYear = extras.getInt(CalendarThaiAction.NUM_YEAR_GROUP, 20);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, -numOfYear);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs.edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListYear();
        } else if (CalendarThaiAction.ACTION_NEXT_YEAR_GROUP.equals(action)
                && extras != null && extras.containsKey(CalendarThaiAction.NUM_YEAR_GROUP)) {
            int numOfYear = extras.getInt(CalendarThaiAction.NUM_YEAR_GROUP, 20);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, sharedPrefs.getInt(CalendarThaiAction.PREF_DATE, 1));
            cal.set(Calendar.MONTH, sharedPrefs.getInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH)));
            cal.set(Calendar.YEAR, sharedPrefs.getInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, numOfYear);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .apply();
            } else {
                sharedPrefs
                        .edit()
                        .putInt(CalendarThaiAction.PREF_DATE, cal.get(Calendar.DATE))
                        .putInt(CalendarThaiAction.PREF_MONTH, cal.get(Calendar.MONTH))
                        .putInt(CalendarThaiAction.PREF_YEAR, cal.get(Calendar.YEAR))
                        .commit();
            }
            drawWidgetListYear();
        }
    }

    public void onAddEventClicked(View view){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(Events.TITLE, "Neel Birthday");
        intent.putExtra(Events.DESCRIPTION,  "This is a sample description");
        intent.putExtra(Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }
}
