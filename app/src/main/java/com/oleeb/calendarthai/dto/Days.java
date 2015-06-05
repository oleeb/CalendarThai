package com.oleeb.calendarthai.dto;

import android.os.Bundle;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class Days {
    public Bundle data = new Bundle();
    private Long time;
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
}
