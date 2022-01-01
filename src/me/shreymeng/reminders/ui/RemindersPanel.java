package me.shreymeng.reminders.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;
import me.shreymeng.reminders.util.Common;

public class RemindersPanel extends JPanel {

  private SortBy sortBy = SortBy.DUE_DATE;
  private JTabbedPane currentCategoryTabs;

  public RemindersPanel() {
    setLayout(new BorderLayout());
    add(new SortByPanel(), BorderLayout.PAGE_START);
    refresh();
  }

  private void refresh() {

    if (currentCategoryTabs != null) {
      remove(currentCategoryTabs);
      revalidate();
      repaint();
    }

    final List<Reminder> reminders = RemindersManager.getReminders(sortBy);

    final JTabbedPane categoryTabs = new JTabbedPane();
    categoryTabs.add("All", new RemindersListPanel(reminders));

    LabelsManager.getLabels().stream().filter(Label::isCategory).forEachOrdered(label ->
        categoryTabs.add(label.getName(),
            new RemindersListPanel(reminders.stream()
                .filter(reminder -> reminder.getLabels().contains(label))
                .collect(Collectors.toList()))));

    currentCategoryTabs = categoryTabs;
    add(categoryTabs);
  }

  private class SortByPanel extends JPanel {

    public SortByPanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT));
      add(new JLabel("Sort By:"));

      final JComboBox<String> selector = new JComboBox<>(
          Arrays.stream(SortBy.values()).map(SortBy::toString).toArray(String[]::new));
      selector.setSelectedItem(SortBy.DUE_DATE);
      selector.addActionListener(e -> {
        sortBy = SortBy.values()[selector.getSelectedIndex()];
        refresh();
      });

      add(selector);
    }
  }

  private static class RemindersListPanel extends JPanel {

    public RemindersListPanel(List<Reminder> reminders) {

      final JButton addButton = new JButton("+ New Reminder");
      addButton.setPreferredSize(new Dimension(900, 25));

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      for (Reminder reminder : reminders) {
        panel.add(reminderPanel(reminder));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
      }

      final JScrollPane scrollPane = new JScrollPane(panel);
      scrollPane.setPreferredSize(new Dimension(1100, 550));
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      scrollPane.setBorder(
          BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
              BorderFactory.createEmptyBorder(10, 10, 0, 10)));

      add(addButton);
      add(scrollPane);
    }

    private JPanel reminderPanel(Reminder reminder) {

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setMinimumSize(new Dimension(1000, 105));
      panel.setMaximumSize(new Dimension(1000, 105));

      final JLabel titleLabel = new JLabel(reminder.getTask());
      titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
      titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      final JLabel descriptionLabel = new JLabel(reminder.getDescription());
      descriptionLabel.setForeground(Color.GRAY);
      descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
      descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      final JLabel dueDateLabel = new JLabel(
          "Due: " + Common.formatDateTime(reminder.getDueDate()));
      dueDateLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
      dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

      final JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

      for (Label label : LabelsManager.getLabels()) {
        final JLabel labelLabel = new JLabel("❚ " + label.getName() + "  ");
        labelLabel.setForeground(label.getColor());
        labelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        footer.add(labelLabel);
      }

      final JLabel priorityLabel = new JLabel("  " + reminder.getPriority().toString());
      priorityLabel.setForeground(reminder.getPriority().getColor());
      priorityLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
      footer.add(priorityLabel);

      final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
      final JButton completeButton = new JButton("✔");
      final JButton editButton = new JButton("Edit");

      buttons.add(completeButton);
      buttons.add(editButton);

      footer.setAlignmentX(Component.LEFT_ALIGNMENT);
      buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

      panel.add(titleLabel);
      panel.add(descriptionLabel);
      panel.add(dueDateLabel);
      panel.add(footer);
      panel.add(buttons);

      return panel;
    }
  }
}
