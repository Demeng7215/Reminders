package me.shreymeng.reminders.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * The main GUI where all reminders will be displayed.
 */
public class MainGUI {

  public MainGUI() {

    final JFrame frame = new JFrame("My Reminders");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setSize(1280, 720);
    frame.setLocationRelativeTo(null);

    final JTabbedPane tabs = new JTabbedPane();
    tabs.add("My Reminders", new RemindersPanel());
    //TODO Add calendar view.
    tabs.add("Calendar", new JPanel());

    frame.add(tabs);
    frame.setVisible(true);
  }
}
