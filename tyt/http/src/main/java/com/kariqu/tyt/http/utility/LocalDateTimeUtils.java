package com.kariqu.tyt.http.utility;

import org.apache.tomcat.jni.Local;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeUtils {

    //默认使用系统当前时区
    private static final ZoneId ZONE = ZoneId.systemDefault();

    public static LocalDateTime dateToLocalDateTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static boolean isSameDay(LocalDateTime d1, LocalDateTime d2) {
        return d1.getYear() == d2.getYear()
                && d1.getMonthValue() == d2.getMonthValue()
                && d1.getDayOfMonth() == d2.getDayOfMonth();
    }


    public static Date stringToMysqlDatetime(String str) {
        return stringToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    public static String mysqlDatetimeToString(Date d) {
        return dateToString(d, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToString(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static Date stringToDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d = sdf.parse(str);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
