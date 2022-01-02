package me.shreymeng.reminders.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * General and commonly used utilities.
 */
public class Common {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
      "MMM d yyyy");
  private static final DateTimeFormatter DATE_NO_YEAR_FORMAT = DateTimeFormatter.ofPattern(
      "MMM d");
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
      "MMM d yyyy h:mma");
  private static final DateTimeFormatter DATE_TIME_NO_YEAR_FORMAT = DateTimeFormatter.ofPattern(
      "MMM d h:mma");

  private static final String ASSETS_FOLDER = "assets";

  /**
   * Formats a date and time.
   *
   * @param dateTime The date and time to format
   * @return The formatted date, and time if applicable
   */
  public static String formatDateTime(long dateTime) {

    final String FORMATTED;

    final LocalDateTime LOCAL_DATE = new Date(dateTime)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    final boolean INCLUDE_TIME = !(LOCAL_DATE.getHour() == 0 && LOCAL_DATE.getMinute() == 0);

    if (LOCAL_DATE.getYear() == LOCAL_DATE.now().getYear()) {
      FORMATTED = LOCAL_DATE.format(INCLUDE_TIME ? DATE_TIME_NO_YEAR_FORMAT : DATE_NO_YEAR_FORMAT);
    } else {
      FORMATTED = LOCAL_DATE.format(INCLUDE_TIME ? DATE_TIME_FORMAT : DATE_FORMAT);
    }

    return FORMATTED.replace(".", "");
  }

  /**
   * Gets an image from the assets folder and wraps it inside a JLabel.
   *
   * @param name The file name inside the assets folder
   * @return The image, wrapped in a JLabel
   */
  public static JLabel getImage(String name) {
    try {
      return new JLabel(
          new ImageIcon(ImageIO.read(new File(ASSETS_FOLDER + File.separator + name))));
    } catch (IOException ex) {
      ex.printStackTrace();
      System.err.println("Failed to read image: " + name);
      return null;
    }
  }
}
