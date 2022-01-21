package me.shreymeng.reminders;

import java.awt.EventQueue;
import java.awt.Frame;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import me.shreymeng.reminders.data.LabelsDataFile;
import me.shreymeng.reminders.data.RemindersDataFile;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.ui.TitleFrame;

public class Main {

  public static final String VERSION = "2.0-2022-01-17";

  /**
   * The data file containing all information on labels.
   */
  private static LabelsDataFile labelsDataFile;
  /**
   * The data file containing all information on reminders.
   */
  private static RemindersDataFile remindersDataFile;

  /**
   * The main GUI frame of the application.
   */
  private static Frame mainFrame;

  public static void main(String[] args) {

    // Set the look and feel of the GUI.
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      System.err.println("Unsupported operating system.");
    }

    try {
      // Load data files.
      labelsDataFile = new LabelsDataFile();
      remindersDataFile = new RemindersDataFile();

      // Add all labels saved in the file.
      LabelsManager.getLabels().addAll(labelsDataFile.load());

      // Add all reminders saved in the file.
      for (Reminder reminder : remindersDataFile.load()) {
        RemindersManager.getRemindersMap().put(reminder.getId(), reminder);
      }

    } catch (IOException ex) {
      System.err.println("Failed to load data: " + ex.getMessage());
    }

    // Create and open the title GUI.
    EventQueue.invokeLater(TitleFrame::new);
  }

  public static LabelsDataFile getLabelsDataFile() {
    return labelsDataFile;
  }

  public static RemindersDataFile getRemindersDataFile() {
    return remindersDataFile;
  }

  public static Frame getMainFrame() {
    return mainFrame;
  }

  public static void setMainFrame(Frame newMainFrame) {
    mainFrame = newMainFrame;
  }
}
