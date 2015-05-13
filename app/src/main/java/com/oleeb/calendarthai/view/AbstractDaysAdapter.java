package com.oleeb.calendarthai.view;

import android.content.Context;

/**
 * Created by OLEEB on 6/27/2014.
 */
public abstract class AbstractDaysAdapter implements DaysAdapter {
    private Context context;
    public AbstractDaysAdapter(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }
}
