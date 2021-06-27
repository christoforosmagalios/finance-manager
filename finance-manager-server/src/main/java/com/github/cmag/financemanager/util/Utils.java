package com.github.cmag.financemanager.util;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * Contains utility methods.
 */
public class Utils {

  private Utils() {
    // default constructor.
  }

  /**
   * Get the first day of the given month and year.
   *
   * @param month The month.
   * @param year the year.
   * @return The first day of the month.
   */
  public static LocalDate getFirstDayOfMonth(int month, int year) {
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    return yearMonth.atDay(1);
  }

  /**
   * Get the last day of the given month and year.
   *
   * @param month The month.
   * @param year the year.
   * @return The last day of the month.
   */
  public static LocalDate getLastDayOfMonth(int month, int year) {
    // Get first and last of the current month.
    YearMonth yearMonth = YearMonth.of(year, month);
    return yearMonth.atEndOfMonth();
  }

  /**
   * Get the last day of the current month and year.
   *
   * @return The last day of the month.
   */
  public static LocalDate getLastDayOfMonth() {
    // Get current year.
    int year = Calendar.getInstance().get(Calendar.YEAR);
    // Get current month.
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    return getLastDayOfMonth(month, year);
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
    return getFirstDayOfYear(year);
  }

  /**
   * Get the first day of the given year.
   *
   * @param year The year.
   * @return The date.
   */
  public static LocalDate getFirstDayOfYear(int year) {
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
    return getLastDayOfYear(year);
  }

  /**
   * Get the last day of the given year.
   *
   * @param year The year.
   * @return The date.
   */
  public static LocalDate getLastDayOfYear(int year) {
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

  /**
   * Get the name of the given month.
   *
   * @param num The month index.
   * @return The name of the month.
   */
  public static String getMonthName(int num) {
    return getMonthName(num, new Locale("en"));
  }

  /**
   * Get the name of the given month for the given Locale.
   *
   * @param num The month index.
   * @param locale The Locale.
   * @return The name of the month.
   */
  public static String getMonthName(int num, Locale locale) {
    String month = "";
    DateFormatSymbols dfs = new DateFormatSymbols(locale);
    String[] months = dfs.getMonths();
    if (num >= 0 && num <= 11) {
      month = months[num];
    }
    return month;
  }
}
