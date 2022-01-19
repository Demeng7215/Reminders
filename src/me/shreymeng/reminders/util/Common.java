package me.shreymeng.reminders.util;

import java.awt.Component;
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
import javax.swing.JOptionPane;

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

    String formatted;

    LocalDateTime localDate = new Date(dateTime)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    boolean includeTime = !(localDate.getHour() == 0 && localDate.getMinute() == 0);

    if (localDate.getYear() == LocalDate.now().getYear()) {
      formatted = localDate.format(includeTime ? DATE_TIME_NO_YEAR_FORMAT : DATE_NO_YEAR_FORMAT);
    } else {
      formatted = localDate.format(includeTime ? DATE_TIME_FORMAT : DATE_FORMAT);
    }

    return formatted.replace(".", "");
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

  /**
   * Open a confirmation dialog for the user.
   *
   * @param parent      The component asking for confirmation
   * @param title       The title of the dialog
   * @param message     The message of the dialog
   * @param confirmTask The task to run on confirmation
   */
  public static void askConfirmation(Component parent, String title, String message,
      Runnable confirmTask) {

    final int option = JOptionPane.showConfirmDialog(parent, message, title,
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

    if (option == JOptionPane.YES_OPTION) {
      confirmTask.run();
    }
  }
}
