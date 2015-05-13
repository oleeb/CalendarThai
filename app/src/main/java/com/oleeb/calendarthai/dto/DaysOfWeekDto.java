package com.oleeb.calendarthai.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class DaysOfWeekDto {
    public List<Days> daysOfWeek = new LinkedList<Days>();

    public int getMaximumDaysOfWeek() {
        return daysOfWeek.size();
    }

}
