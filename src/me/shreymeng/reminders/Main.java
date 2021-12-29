package me.shreymeng.reminders;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import me.shreymeng.reminders.ui.TitleMenu;

public class Main {

  public static final String VERSION = "1.0-2022-01-04";

  public static void main(String[] args) {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
      System.err.println("Unsupported operating system.");
    }

    EventQueue.invokeLater(TitleMenu::new);
  }
}
