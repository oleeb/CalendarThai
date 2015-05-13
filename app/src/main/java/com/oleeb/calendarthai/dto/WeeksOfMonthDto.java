package com.oleeb.calendarthai.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by OLEEB on 6/27/2014.
 */
public class WeeksOfMonthDto {
    public List<DaysOfWeekDto> weeksOfMonth = new LinkedList<DaysOfWeekDto>();

    public int getMaximumWeeksOfMonth() {
        return weeksOfMonth.size();
    }
}
