package com.lsmsdbgroup.pisaflix.pisaflixservices;

import java.time.*;
import java.util.*;

public class DateConverter {
    public static Date LocalDateToDate(LocalDate ld){
        if(ld == null)
            return null;
        return java.util.Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}
