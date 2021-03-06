/**
 * Copyright (c) 2014 CoderKiss
 * <p>
 * CoderKiss[AT]gmail.com
 */

package com.applikey.mattermost.utils.kissUtils.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static final String TAG = TimeUtil.class.getSimpleName();
    public static final String DEFAULT_FORMAT_TIME_ONLY = "hh:mm aa";
    public static final String DEFAULT_FORMAT_DATE_ONLY = "dd, MMMM yyyy";
    public static final String CHANNEL_DATE_FORMAT = "d.MM.yyyy";
    public static final String DEFAULT_FORMAT_DATE_TIME = "hh:mm aa, dd, MMMM yyyy";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String FILE_FORMAT = "yyyy_MM_dd_HH_mm_ss_SSS";
    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    private TimeUtil() {
    }

    public static long format(String time, String format) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        long modified = 0;
        try {
            final Date date = dateFormat.parse(time);
            modified = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modified;
    }

    public static String format(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String formatTimeOnly(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_TIME_ONLY, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String formatDateOnly(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_DATE_ONLY, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String formatDateOnlyChannel(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(CHANNEL_DATE_FORMAT, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String formatDateTime(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_DATE_TIME, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String formatTimeOrDateTime(long time) {
        final Date now = new Date();

        final boolean sameDay = time / MILLISECONDS_IN_DAY ==
                now.getTime() / MILLISECONDS_IN_DAY;

        if (sameDay) {
            return formatTimeOnly(time);
        } else {
            return formatDateTime(time);
        }
    }

    public static String formatTimeOrDateOnlyChannel(long time) {

        final boolean sameDay = DateUtils.isToday(time);

        if (sameDay) {
            return formatTimeOnly(time);
        } else {
            return formatDateOnlyChannel(time);
        }
    }

    public static String formatTimeOrDateOnly(long time) {
        final Date now = new Date();

        final boolean sameDay = time / MILLISECONDS_IN_DAY ==
                now.getTime() / MILLISECONDS_IN_DAY;

        if (sameDay) {
            return formatTimeOnly(time);
        } else {
            return formatDateOnly(time);
        }
    }

    public static String format(long time, String format) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        final Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String format(Date date, String format) {
        if (TextUtils.isEmpty(format) || date == null) {
            return null;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date);
    }

    public static String format(String timeStr, String srcFormat,
            String dstFormat) {
        final long time = format(timeStr, srcFormat);
        //noinspection UnnecessaryLocalVariable
        final String result = format(time, dstFormat);
        return result;
    }

    public static String utcToLocal(String utcTime) {
        String localTime = null;
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_FORMAT, Locale.US);
            final Date date = dateFormat.parse(utcTime);
            dateFormat.applyPattern(DEFAULT_FORMAT);
            localTime = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTime;
    }

    public static boolean sameTime(long timePrev, long timeCurrent) {
        return (timePrev / 1000 / 60) == (timeCurrent / 1000 / 60);
    }

    public static boolean sameDate(long timePrev, long timeCurrent) {
        return (timePrev / 1000 / 60 / 60 / 24) == (timeCurrent / 1000 / 60 / 60 / 24);
    }
}
