package com.phungdung.gnoc2.common.untils;

import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Log4j
public class DateUtils {
    public static Date converstStringToDate(String dateTime, String format) {
        try {
            if (StringUtils.isStringNotNullOrEmtry(dateTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(dateTime));
                return calendar.getTime();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String converstDateToString(Date dateTime, String format) {
        try {
            if (!StringUtils.isStringNullOrEmtry(dateTime)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateTime);
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(calendar.getTime());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getSysDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return converstDateToString(calendar.getTime(), "dd/MM/yyyy HH:mm:ss");
    }
}
