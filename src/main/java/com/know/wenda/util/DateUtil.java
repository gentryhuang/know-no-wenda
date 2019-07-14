
package com.know.wenda.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * DateUtil
 *
 * @author hlb
 */
public class DateUtil {

    public static String MONTH_DAY = "M月dd日";

    public static String NEW_MONTH_DAY = "M.dd";

    public static String MONTH_DAY_SPRIT = "MM/dd";

    public static String YYYY_MM_DD_DAY = "yyyy-MM-dd";

    public static String YYYYMMDD_DAY = "yyyyMMdd";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String HH_MM = "HH:mm";

    public static String CHINA_MM_DD_HH_MM = "M月d日 HH:mm";

    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String MM_DD = "MM-dd";

    public static final String MMDD_HHMM = "MM-dd HH:mm";

    public static String format(LocalDate localDate, String format) {
        return _format(localDate, format);
    }


    public static String format(LocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }


    public static LocalDate parse(String dateStr, DateTimeFormatter formatter) {
        return LocalDate.parse(dateStr, formatter);
    }

    private static String _format(LocalDate date, String formatStr) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(formatStr);
            return format(date, format);
        } catch (DateTimeException ex) {
            return StringUtils.EMPTY;
        }
    }

    public static Date localDate2Date(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = date.atTime(0, 0, 0).atZone(zone);
        return Date.from(Instant.from(zdt));
    }

    public static LocalDateTime Date2LocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static String timeMillis4YYYYMMDD(long timeLong) {

        LocalDateTime localDateTime = Date2LocalDateTime(new Date(timeLong));
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYYMMDD_DAY);
        return localDateTime.format(format);
    }

    public static String timeMillis4YYYY_MM_DD(long timeLong) {

        LocalDateTime localDateTime = Date2LocalDateTime(new Date(timeLong));
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYY_MM_DD_DAY);
        return localDateTime.format(format);
    }

    public static String timeMillisFormat(long timeLong, String formatStr) {

        LocalDateTime localDateTime = Date2LocalDateTime(new Date(timeLong));
        DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM);
        return localDateTime.format(format);
    }

    public static Long getLocalDateTimeMilli(LocalDateTime dateTime) {
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        return timestamp.getTime();
    }

    /**
     * 将时间戳转换成 LocalDate 对象
     *
     * @param timestamps 时间戳
     * @return LocalDate 对象
     */
    public static LocalDate timestamps2LocalDate(long timestamps) {
        return Date2LocalDate(new Date(timestamps));
    }

    /**
     * 将时间戳转换成 LocalDate 对象
     *
     * @param localDate 对象
     * @return timestamps 时间戳
     */
    public static long LocalDate2timestamps(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date.getTime();
    }

    private static String timeMillis4FormatStr(long timeLong, String formatStr) {
        LocalDateTime localDateTime = Date2LocalDateTime(new Date(timeLong));
        DateTimeFormatter format = DateTimeFormatter.ofPattern(formatStr);
        return localDateTime.format(format);
    }

    public static String timeMillis4HH_MM(long timeLong) {
        return timeMillis4FormatStr(timeLong, HH_MM);
    }


    public static String timeMillis4CHINA_MM_DD_HH_MM(long timeLong) {
        return timeMillis4FormatStr(timeLong, CHINA_MM_DD_HH_MM);
    }

    public static String timeMillis4YYYY_MM_DD_HH_MM_SS(long timeLong) {
        return timeMillis4FormatStr(timeLong, YYYY_MM_DD_HH_MM_SS);
    }

    public static String timeMillis4YYYY_MM_DD_HH_MM(long timeLong) {
        return timeMillis4FormatStr(timeLong, YYYY_MM_DD_HH_MM);
    }

    public static long YYYY_MM_DD_HH_MM_SS4timeMillis(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            return sdf.parse(timeStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static LocalDate Date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    /**
     * 将一个 LocalDate 对象转换成时间戳
     * 这里只是将日期装换成时间戳
     *
     * @param localDate localDate
     * @return 时间戳
     */
    public static long LocalDate2Timestamps(LocalDate localDate) {
        String[] localDateArray = localDate.toString().split("-");
        int year = Integer.parseInt(localDateArray[0]);
        int month = Integer.parseInt(localDateArray[1]);
        int dayOfMonth = Integer.parseInt(localDateArray[2]);
        LocalDateTime dateTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0);
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        return timestamp.getTime();
    }

    /**
     * 获取两个日期之间所有日期的数组
     *
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDate> getBetweenDate(LocalDate start, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(start, end);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(start, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add((LocalDate) f);
        });
        return list;
    }

    /**
     * 获取两个日期之间所有日期时间戳的数组
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Long> getBetweenDateTimestamps(LocalDate start, LocalDate end) {
        List<Long> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(start, end);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(start, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(DateUtil.LocalDate2Timestamps(f));
        });
        return list;
    }

    /**
     * 获取时间戳对应时间当天的24点
     *
     * @param time
     * @return
     */
    public static long get24Clock(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }



    public static long getBetweenDay(LocalDate firstDay, LocalDate lastDay) {
        return (lastDay.toEpochDay() - firstDay.toEpochDay());
    }
}

    