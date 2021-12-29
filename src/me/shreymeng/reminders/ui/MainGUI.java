package me.shreymeng.reminders.ui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainGUI {

  public MainGUI() {

    final JFrame frame = new JFrame("My Reminders");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(1280, 720);
    frame.setLocationRelativeTo(null);

    frame.setVisible(true);
  }
}
