package com.oleeb.calendarthai.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.oleeb.calendarthai.syncdata.CalendarThaiSyncData;

/**
 * Created by HackerOne on 10/26/2015.
 */
public class CalendarThaiAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    Context context;
    ProgressBar bar;
    public CalendarThaiAsyncTask(Context context){
        super();
        this.context = context;
    }

    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.bar != null) {
            bar.setProgress(values[0]);
        }
    }
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d(CalendarThaiAsyncTask.class.getName(),"doInBackground params"+params);
        CalendarThaiSyncData calendarThaiSyncData = new CalendarThaiSyncData(this.context);
        calendarThaiSyncData.run();
        return null;
    }
}
