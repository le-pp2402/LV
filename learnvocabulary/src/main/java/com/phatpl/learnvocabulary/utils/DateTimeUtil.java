package com.phatpl.learnvocabulary.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public String toString(Date date, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = null;
        try {
            dateString = sdf.format(date);
        } catch (Exception e) {
            Logger.logException(DateTimeUtil.class, e);
        }
        return dateString;
    }

    public Date toDateUtil(String dateString, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            Logger.logException(DateTimeUtil.class, e);
        }
        return date;
    }

    public java.sql.Date toDateSQL(String dateString, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        java.sql.Date res = null;
        try {
            Date date = sdf.parse(dateString);
            res = (java.sql.Date) date;
        } catch (Exception e) {
            Logger.logException(DateTimeUtil.class, e);
        }
        return res;
    }

}
