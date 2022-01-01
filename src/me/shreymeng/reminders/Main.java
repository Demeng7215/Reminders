package me.shreymeng.reminders;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import me.shreymeng.reminders.ui.TitleGUI;

//TODO Remove in production.
public class Main {

  public static final String VERSION = "1.0-2022-01-04";

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
    EventQueue.invokeLater(TitleGUI::new);
  }
}
