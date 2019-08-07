package com.sina.shopguide.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.text.TextUtils;
import android.util.Log;


public class DateUtils {

  private final static String TAG = DateUtils.class.getName();

  /**
   * 获取下一天的开始时间 00:00:00.000
   */
  public static long getNextDayStartTime() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTimeInMillis();
  }

  /**
   * 按照指定的时间格式转换成Date对象
   * 
   * @param format 格式字符串
   * @param dateStr 时间字符串
   * @return
   * @throws ParseException
   */
  public static Date format(String format, String dateStr) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
    Date date = null;
    try {
      date = sdf.parse(dateStr);
    } catch (ParseException e) {
      Log.e(TAG, "format error ", e);
    }
    return date;
  }

  /**
   * 将日期分解成 年、月、日、时、分、秒的int数组，用于发给前台时间
   * 
   * @param date
   * @return
   */
  public static int[] splitDate(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int[] dates = new int[6];
    dates[0] = c.get(Calendar.YEAR);// 年
    dates[1] = c.get(Calendar.MONTH) + 1;// 月
    dates[2] = c.get(Calendar.DAY_OF_MONTH);// 日
    dates[3] = c.get(Calendar.HOUR_OF_DAY);// 小时
    dates[4] = c.get(Calendar.MINUTE);// 分
    dates[5] = c.get(Calendar.SECOND);// 秒
    return dates;
  }

  /**
   * 判断指定日期是否是指定星期几
   * 
   * @param week
   * @return
   */
  public static boolean isWeekDay(Date date, int week) {
    boolean flag = false;
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    if (week == getDateToWeek(c)) {
      flag = true;
    }

    return flag;
  }

  /**
   * 判断两个日期是否是同一周
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isDiffWeek(Date date1, Date date2) {
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(getFirstDayOfWeek(date1));
    GregorianCalendar gc1 = (GregorianCalendar) Calendar.getInstance();
    gc1.setTime(getFirstDayOfWeek(date2));
    if (gc.getTime().getTime() == gc1.getTime().getTime()) {
      return true;
    }
    return false;
  }

  /**
   * 得到输入日期是一个星期的第几天
   * 
   * @param gc
   * @return
   */
  public static int getDateToWeek(Calendar gc) {
    switch (gc.get(Calendar.DAY_OF_WEEK)) {
      case (Calendar.SUNDAY):
        return 7;
      case (Calendar.MONDAY):
        return 1;
      case (Calendar.TUESDAY):
        return 2;
      case (Calendar.WEDNESDAY):
        return 3;
      case (Calendar.THURSDAY):
        return 4;
      case (Calendar.FRIDAY):
        return 5;
      case (Calendar.SATURDAY):
        return 6;
    }
    return 0;
  }

  /**
   * 得到输入日期这个星期的星期一
   * 
   * @param date
   * @return
   */
  public static Date getFirstDayOfWeek(Date date) {
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    switch (gc.get(Calendar.DAY_OF_WEEK)) {
      case (Calendar.SUNDAY):
        gc.add(Calendar.DATE, -6);
        break;
      case (Calendar.MONDAY):
        gc.add(Calendar.DATE, 0);
        break;
      case (Calendar.TUESDAY):
        gc.add(Calendar.DATE, -1);
        break;
      case (Calendar.WEDNESDAY):
        gc.add(Calendar.DATE, -2);
        break;
      case (Calendar.THURSDAY):
        gc.add(Calendar.DATE, -3);
        break;
      case (Calendar.FRIDAY):
        gc.add(Calendar.DATE, -4);
        break;
      case (Calendar.SATURDAY):
        gc.add(Calendar.DATE, -5);
        break;
    }

    return gc.getTime();
  }

  /**
   * 得到这个日期的最后一秒
   * 
   * @param date
   * @return
   */
  public static Date getTodayEnd(Date date) {
    String day = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    day = day + " 23:59:59";
    Date result = null;
    try {
      result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(day);
    } catch (ParseException e) {
    }
    return result;
  }

  /**
   * 得到这个日期的第一秒
   * 
   * @param date
   * @return
   */
  public static Date getTodayStart(Date date) {
    String day = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    day = day + " 00:00:00";
    Date result = null;
    try {
      result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(day);
    } catch (ParseException e) {
      Log.e(TAG, "format error ", e);
    }
    return result;
  }

  /**
   * 获取间隔一定天数的日期
   * 
   * @param date
   * @param day
   * @return
   */
  public static Date getIntervalDay(Date date, long day) {
    Date result = new Date();
    result.setTime(date.getTime() + day * 1000 * 24 * 60 * 60);
    return result;
  }

  /**
   * 获取间隔一定分钟的日期
   * 
   * @param date
   * @param day
   * @return
   */
  public static Date getIntervalMinute(Date date, long minute) {
    Date result = new Date();
    result.setTime(date.getTime() + minute * 1000 * 60);
    return result;
  }

  /**
   * 获取两个时间的天数差
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static int getDateDiff(final Date dateA, final Date dateB) {
    Date date1 = dateA;
    Date date2 = dateB;
    if (date1.getTime() > date2.getTime()) {
      date1 = getTodayEnd(date1);
    } else {
      date2 = getTodayEnd(date2);
    }
    int i = Math.abs((int) ((date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24));
    return i;
  }

  /**
   * 
   * 判断两个日期是否是同一天
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isSameDay(Date date1, Date date2) {
    return getDateDiff(date1, date2) == 0 ? true : false;
  }

  /**
   * 获取两个时间的小时差
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static int getHourDiff(Date date1, Date date2) {
    int i = Math.abs((int) ((date1.getTime() - date2.getTime()) / 1000 / 60 / 60));
    return i;
  }

  /**
   * 获取两个时间的分钟差
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static int getMinuteDiff(Date date1, Date date2) {
    int i = Math.abs((int) ((date1.getTime() - date2.getTime()) / 1000 / 60));
    return i;
  }

  /**
   * 获取两个时间的秒数差
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static int getSecondDiff(Date date1, Date date2) {
    int i = Math.abs((int) ((date1.getTime() - date2.getTime()) / 1000));
    return i;
  }

  /**
   * 
   * 判断是否是零时
   * 
   * @return
   */
  public static boolean isWeeHour() {
    Calendar calendar = Calendar.getInstance();

    if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0
        && calendar.get(Calendar.SECOND) == 0) {
      return true;
    }

    return false;
  }

  /**
   * 将时间毫秒格式化为字符串
   * 
   * @param format 时间格式
   * @param time 时间毫秒
   * @return
   */
  public static String format(String format, long time) {
    return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(time));
  }

  /**
   * 格式化时间
   * 
   * @param startTime
   * @return
   */
  public static String format(long time) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date(
        time));
  }

  /**
   * 格式化到精确到秒的时间字符串
   * 
   * @param 时间秒
   * @return
   */
  public static String formatBySec(long time) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(time));
  }

  /**
   * 格式化到精确到秒的时间字符串
   * 
   * @param 时间秒
   * @return
   */
  public static String formatByDay(long time) {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(time));
  }

  /**
   * 解析格式时间字符串
   * 
   * @param format
   * @param date
   * @return 时间毫秒
   * @throws ParseException
   */
  public static long parse(String format, String date) throws ParseException {
    return new SimpleDateFormat(format, Locale.getDefault()).parse(date).getTime();
  }

  /**
   * 获取下一个偶数整点时间
   */
  public static long getNextEvenTime() {
    Calendar cal = Calendar.getInstance();
    if (cal.get(Calendar.HOUR_OF_DAY) % 2 != 0) {
      cal.add(Calendar.HOUR_OF_DAY, 1);
    } else {
      cal.add(Calendar.HOUR_OF_DAY, 2);
    }
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTimeInMillis();
  }

  /**
   * 根据自定义格式将日期转为字符串，默认为 yyyy-MM-dd
   * 
   * @param date 日期
   * @param format 日期字符串格式
   * @return String
   */
  public static String dateFormat(Date date, String format) {
    SimpleDateFormat fmt;

    if (TextUtils.isEmpty(format)) {
      fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    } else {
      fmt = new SimpleDateFormat(format, Locale.getDefault());
    }

    return fmt.format(date);
  }

  /**
   * 获取某一天的0时0分0秒日期
   * 
   * @param day 1明天日期，0当天日期，-1昨天日期
   * @return
   */
  public static Date getOneDayTime(int day) {
    Calendar calendar = Calendar.getInstance();

    calendar.add(Calendar.DAY_OF_MONTH, day);

    calendar.set(Calendar.HOUR_OF_DAY, 0);

    calendar.set(Calendar.MINUTE, 0);

    calendar.set(Calendar.SECOND, 0);

    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }
}
