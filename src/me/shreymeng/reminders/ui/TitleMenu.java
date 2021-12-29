package me.shreymeng.reminders.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.util.Common;

public class TitleMenu {

  public TitleMenu() {

    final JFrame frame = new JFrame("Reminders (v" + Main.VERSION + ")");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(1280, 720);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // App logo.
    final JPanel logoPanel = new JPanel();
    logoPanel.add(Common.getImage("logo.png"));
    logoPanel.setBounds(390, 200, 500, 100);
    frame.add(logoPanel);

    // Authors, course, and teacher information.
    final JPanel infoPanel = new JPanel();
    final JLabel infoLabel = new JLabel("<html>"
        + "<p><b>Authors:</b> Demeng & Shreya</p></br>"
        + "<p><b>Course:</b> ICS3U7-02</p></br>"
        + "<p><b>Teacher:</b> Ms. Xie</p>"
        + "</html>");
    infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    infoPanel.add(infoLabel);
    infoPanel.setBounds(0, 595, 250, 90);
    frame.add(infoPanel);

    // Version information.
    final JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    final JLabel versionLabel = new JLabel("Version " + Main.VERSION);
    versionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    versionLabel.setForeground(Color.BLUE);
    versionPanel.add(versionLabel);
    versionPanel.setBounds(1010, 650, 250, 30);
    frame.add(versionPanel);

    final JButton button = new JButton("Start");
    button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
    button.setBounds(502, 325, 275, 75);
    frame.add(button);

    frame.setVisible(true);
  }
}
