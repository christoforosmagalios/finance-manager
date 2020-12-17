package com.github.cmag.financemanager.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains utility methods.
 */
public class Utils {

  /**
   * Get the first day of the current month in milliseconds.
   *
   * @return The date in milliseconds.
   */
  public static long getFirstDayOfMonthInMil() {
    return getFirstDayOfMonth().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Get the first day of the current month.
   *
   * @return The first day of the month.
   */
  public static LocalDateTime getFirstDayOfMonth() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate first = yearMonth.atDay(1);
    return first.atStartOfDay();
  }

  /**
   * Get the first day of the month.
   *
   * @return An integer date representation with format yyyyMMdd.
   */
  public static int getFirstDayOfMonthReversed() {
    Date first = Timestamp.valueOf(getFirstDayOfMonth());
    return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(first));
  }

  /**
   * Get the last day of the current month in milliseconds.
   *
   * @return The date in milliseconds.
   */
  public static long getLastDayOfMonthInMil() {
    return getLastDayOfMonth().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Get the last day of the current month.
   *
   * @return The last day of the month.
   */
  public static LocalDateTime getLastDayOfMonth() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate last = yearMonth.atEndOfMonth();
    return last.atTime(23, 59);
  }

  /**
   * Get the last day of the month.
   *
   * @return An integer date representation with format yyyyMMdd.
   */
  public static int getLastDayOfMonthReversed() {
    Date last = Timestamp.valueOf(getLastDayOfMonth());
    return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(last));
  }

  /**
   * Get the first day of the current year in milliseconds.
   *
   * @return The date in milliseconds.
   */
  public static long getFirstDayOfYearInMil() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, 1);
    LocalDate first = yearMonth.atDay(1);
    return first.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Get the last day of the current year in milliseconds.
   *
   * @return The date in milliseconds.
   */
  public static long getLastDayOfYearInMil() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, 12);
    LocalDate last = yearMonth.atEndOfMonth();
    return last.atTime(23, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Extract the day from the given date and remove any leading zeros.
   *
   * @param date Integer representing a Date with format yyyyMMdd.
   * @return The day of the given date.
   */
  public static String getDayFromReversedIntegerDate(int date) {
    String dateStr = String.valueOf(date);
    if (dateStr.length() > 2) {
      return dateStr.substring(dateStr.length() - 2).replaceFirst("^0+(?!$)", "");
    }
    return "";
  }
}
