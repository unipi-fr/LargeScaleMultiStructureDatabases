package com.lsmsdbgroup.pisaflix.pisaflixservices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static Date StringToDate(String dateStr){
        dateStr = dateStr.split("T")[0];
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        
        try {
            date = format.parse(dateStr);
        } catch (ParseException ex) {
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date;
    }
}
