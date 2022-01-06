package me.shreymeng.reminders.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.util.Common;

/**
 * The splash screen displaying basic project information on startup.
 */
public class TitleFrame {

  public TitleFrame() {

    final JFrame frame = new JFrame("Reminders (v" + Main.VERSION + ")");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setSize(1280, 720);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // App logo.
    final JPanel logoPanel = new JPanel();
    logoPanel.add(Common.getImage("logo.png"));
    logoPanel.setBounds(390, 190, 500, 100);
    frame.add(logoPanel);

    // Description.
    final JLabel description = new JLabel(
        "Keeping track of everything you need to get done.", SwingConstants.CENTER);
    description.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    description.setBounds(290, 295, 700, 30);
    frame.add(description);

    // Authors, course, and teacher information.
    final JPanel infoPanel = new JPanel();
    final JLabel infoLabel = new JLabel("<html>"
        + "<p><b>Authors:</b> Demeng Chen & Shreya Sirgound</p></br>"
        + "<p><b>Course:</b> ICS3U7-02</p></br>"
        + "<p><b>Teacher:</b> Ms. Xie</p>"
        + "</html>");
    infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    infoPanel.add(infoLabel);
    infoPanel.setBounds(0, 595, 380, 90);
    frame.add(infoPanel);

    // Version information.
    final JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    final JLabel versionLabel = new JLabel("Version " + Main.VERSION);
    versionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    versionLabel.setForeground(Color.BLUE);
    versionPanel.add(versionLabel);
    versionPanel.setBounds(1010, 650, 250, 30);
    frame.add(versionPanel);

    // Stand button.
    final JButton button = new JButton("Start");
    button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
    button.setBounds(502, 360, 275, 75);
    button.addActionListener(e -> {
      new MainFrame();
      frame.dispose();
    });
    frame.add(button);

    frame.setVisible(true);
  }
}
