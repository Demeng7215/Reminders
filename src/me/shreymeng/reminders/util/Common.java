package me.shreymeng.reminders.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * General and commonly used utilities.
 */
public class Common {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
      "MMMM dd yyyy");
  private static final DateTimeFormatter DATE_NO_YEAR_FORMAT = DateTimeFormatter.ofPattern(
      "MMMM dd");
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
      "MMMM dd yyyy h:mma");
  private static final DateTimeFormatter DATE_TIME_NO_YEAR_FORMAT = DateTimeFormatter.ofPattern(
      "MMMM dd h:mma");

  /**
   * Formats a date and time.
   *
   * @param dateTime The date and time to format
   * @return The formatted date, and time if applicable
   */
  public static String formatDateTime(long dateTime) {

    final LocalDateTime localDate = new Date(dateTime)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    final boolean includeTime = !(localDate.getHour() == 0 && localDate.getMinute() == 0);

    if (localDate.getYear() == LocalDate.now().getYear()) {
      return localDate.format(includeTime ? DATE_TIME_NO_YEAR_FORMAT : DATE_NO_YEAR_FORMAT);
    }

    return localDate.format(includeTime ? DATE_TIME_FORMAT : DATE_FORMAT);
  }
}
