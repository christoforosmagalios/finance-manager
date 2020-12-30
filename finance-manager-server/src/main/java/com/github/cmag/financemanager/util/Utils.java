package com.github.cmag.financemanager.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Contains utility methods.
 */
public class Utils {

  /**
   * Get the first day of the current month.
   *
   * @return The first day of the month.
   */
  public static LocalDate getFirstDayOfMonth() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    return yearMonth.atDay(1);
  }

  /**
   * Get the last day of the current month.
   *
   * @return The last day of the month.
   */
  public static LocalDate getLastDayOfMonth() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    return yearMonth.atEndOfMonth();
  }

  /**
   * Get the first day of the current year.
   *
   * @return The date.
   */
  public static LocalDate getFirstDayOfYear() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, 1);
    return yearMonth.atDay(1);
  }

  /**
   * Get the last day of the current year.
   *
   * @return The date.
   */
  public static LocalDate getLastDayOfYear() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, 12);
    return yearMonth.atEndOfMonth();
  }

  /**
   * Convert the given Date to a reversed integer representation.
   *
   * @param date The Date to be converted.
   * @return The reversed integer date representation.
   */
  public static int getDateReversed(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return Integer.parseInt(date.format(formatter));
  }
}
