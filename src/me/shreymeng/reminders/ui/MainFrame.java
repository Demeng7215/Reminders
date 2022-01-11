package me.shreymeng.reminders.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.ui.views.CalendarView;
import me.shreymeng.reminders.ui.views.IRemindersView;
import me.shreymeng.reminders.ui.views.ListView;

/**
 * The main GUI frame, where the user will be able to see their reminders in 2 separate views- list
 * and calendar.
 */
public class MainFrame {

  public MainFrame() {

    JFrame frame = new JFrame("My Reminders");
    Main.setMainFrame(frame);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setSize(1280, 720);
    frame.setLocationRelativeTo(null);

    JTabbedPane tabs = new JTabbedPane();
    tabs.add("My Reminders", new ListView());
    tabs.add("Calendar", new CalendarView());

    tabs.addChangeListener(
        e -> ((IRemindersView) tabs.getComponentAt(tabs.getSelectedIndex())).refresh());

    frame.add(tabs);
    frame.setVisible(true);
  }
}
