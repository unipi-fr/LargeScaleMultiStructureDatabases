/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix;

import java.time.*;
import java.util.*;

/**
 *
 * @author FraRonk
 */
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
