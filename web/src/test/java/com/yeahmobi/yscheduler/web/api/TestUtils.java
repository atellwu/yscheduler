package com.yeahmobi.yscheduler.web.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {

    private static final long             SECOND       = 1000;

    private static final SimpleDateFormat sdf          = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Date                           DEFAULT_TIME = new Date();

    static {
        try {
            DEFAULT_TIME = sdf.parse("2009-09-09 00:00:00");
        } catch (ParseException e) {
            // IGNORE
        }
    }

    public static boolean generallyEquals(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime()) < (5 * SECOND);
    }
}
