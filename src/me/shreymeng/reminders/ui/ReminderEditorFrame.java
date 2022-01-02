package me.shreymeng.reminders.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import me.shreymeng.reminders.model.Priority;
import me.shreymeng.reminders.model.Reminder;

/**
 * The frame for creating and editing reminders.
 */
public class ReminderEditorFrame {

  private final Reminder current;

  public ReminderEditorFrame(Reminder current) {
    this.current = current;

    final JFrame frame = new JFrame(current == null ? "Add Reminder" : "Edit Reminder");
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setResizable(false);
    frame.setSize(600, 350);
    frame.setLocationRelativeTo(null);

    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    final JLabel titleLabel = new JLabel("Task");
    final JTextField titleField = new JTextField();
    titleField.setMaximumSize(new Dimension(550, 24));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JLabel descriptionLabel = new JLabel("Description");
    final JTextField descriptionField = new JTextField();
    descriptionField.setMaximumSize(new Dimension(550, 24));
    descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    descriptionField.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JLabel dueDateLabel = new JLabel("Due Date");
    final JPanel dueDatePanel = new DueDatePanel();
    dueDatePanel.setMaximumSize(new Dimension(550, 24));
    dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    dueDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JLabel priorityLabel = new JLabel("Priority");
    final JComboBox<String> priorityDropdown = new JComboBox<>(
        Arrays.stream(Priority.values()).map(Priority::toString).toArray(String[]::new));
    priorityDropdown.setMaximumSize(new Dimension(550, 24));
    priorityDropdown.setSelectedIndex(1);
    priorityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    priorityDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);

    final JLabel labelsLabel = new JLabel("Labels (0 selected)");
    final JButton labelsButton = new JButton("Select Labels...");
    labelsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsButton.setAlignmentX(Component.LEFT_ALIGNMENT);

    panel.add(titleLabel);
    panel.add(titleField);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(descriptionLabel);
    panel.add(descriptionField);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(dueDateLabel);
    panel.add(dueDatePanel);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(priorityLabel);
    panel.add(priorityDropdown);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(labelsLabel);
    panel.add(labelsButton);

    final JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
      //TODO Save.
    });

    frame.add(panel);
    frame.add(saveButton, BorderLayout.PAGE_END);

    frame.setVisible(true);
    frame.setAlwaysOnTop(true);
  }

  /**
   * The panel for selecting due date options.
   */
  private static class DueDatePanel extends JPanel {

    private final JComboBox<Month> monthDropdown;
    private final JComboBox<Integer> dayDropdown;
    private final JComboBox<Integer> yearDropdown;

    private final JComboBox<String> hourDropdown;
    private final JComboBox<String> minuteDropdown;

    public DueDatePanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));

      // The current date and time.
      final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
      // The number of days in the current month.
      final int daysInMonth = YearMonth.of(now.getYear(), now.getMonthValue()).lengthOfMonth();

      this.dayDropdown = new JComboBox<>(
          IntStream.range(1, daysInMonth + 1).boxed().toArray(Integer[]::new));
      dayDropdown.setSelectedItem(now.getDayOfMonth());
      dayDropdown.setPreferredSize(new Dimension(40, 24));

      this.yearDropdown = new JComboBox<>(
          IntStream.range(now.getYear(), now.getYear() + 11).boxed().toArray(Integer[]::new));
      yearDropdown.setSelectedItem(0);
      yearDropdown.setPreferredSize(new Dimension(60, 24));

      this.monthDropdown = new JComboBox<>(Month.values());
      monthDropdown.setSelectedItem(now.getMonth());
      monthDropdown.setPreferredSize(new Dimension(100, 24));
      monthDropdown.addActionListener(e -> {
        // Update daysDropdown to prevent selecting days greater than number of days in the month.

        final Integer selectedYear = (Integer) yearDropdown.getSelectedItem();
        final Month selectedMonth = (Month) monthDropdown.getSelectedItem();

        if (selectedYear == null || selectedMonth == null) {
          return;
        }

        final int monthLength = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth();
        final int selectedDayIndex = dayDropdown.getSelectedIndex();

        dayDropdown.removeAllItems();

        for (int i = 1; i <= monthLength; i++) {
          dayDropdown.addItem(i);
        }

        dayDropdown.setSelectedIndex(Math.min(selectedDayIndex, monthLength - 1));
        repaint();
      });

      this.hourDropdown = new JComboBox<>(
          IntStream.range(0, 24).boxed().map(i -> String.format("%02d", i)).toArray(String[]::new));
      hourDropdown.setSelectedIndex(0);
      hourDropdown.setPreferredSize(new Dimension(40, 24));

      this.minuteDropdown = new JComboBox<>(
          IntStream.range(0, 60).boxed().map(i -> String.format("%02d", i)).toArray(String[]::new));
      minuteDropdown.setSelectedIndex(0);
      minuteDropdown.setPreferredSize(new Dimension(40, 24));

      add(monthDropdown);
      add(dayDropdown);
      add(new JLabel(", "));
      add(yearDropdown);
      add(new JLabel(" @ "));
      add(hourDropdown);
      add(new JLabel(":"));
      add(minuteDropdown);
    }
  }
}
