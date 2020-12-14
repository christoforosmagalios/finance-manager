package com.github.cmag.financemanager.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;

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
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate first = yearMonth.atDay(1);
    return first.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Get the last day of the current month in milliseconds.
   *
   * @return The date in milliseconds.
   */
  public static long getLastDayOfMonthInMil() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate last = yearMonth.atEndOfMonth();
    return last.atTime(23, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
}
