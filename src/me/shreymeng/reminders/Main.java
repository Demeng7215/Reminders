package me.shreymeng.reminders;

import java.awt.EventQueue;
import java.awt.Frame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import me.shreymeng.reminders.ui.TitleFrame;

public class Main {

  public static final String VERSION = "2.0-2022-01-17";

  private static Frame mainFrame;

  public static void main(String[] args) {

    // Set the look and feel of the GUI.
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
      System.err.println("Unsupported operating system.");
    }

    //TODO Remove in production.
    Test.addTestReminders();

    // Create and open the title GUI.
    EventQueue.invokeLater(TitleFrame::new);
  }

  public static Frame getMainFrame() {
    return mainFrame;
  }

  public static void setMainFrame(Frame newMainFrame) {
    mainFrame = newMainFrame;
  }
}
