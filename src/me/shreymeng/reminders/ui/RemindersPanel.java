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

/**
 * The panel displaying reminders as a list.
 */
public class RemindersPanel extends JPanel {

  /**
   * The current sorting method being used.
   */
  private SortBy sortBy = SortBy.DUE_DATE;
  /**
   * The current tabbed pane being displayed.
   */
  private JTabbedPane currentCategoryTabs;

  public RemindersPanel() {
    setLayout(new BorderLayout());
    add(new SortByPanel(), BorderLayout.PAGE_START);
    refresh();
  }

  /**
   * Refreshes the tabbed pane so that the displayed reminders are updated, or a new sorting method
   * has been selected.
   */
  private void refresh() {

    // Remove the current tabbed pane.
    if (currentCategoryTabs != null) {
      remove(currentCategoryTabs);
      revalidate();
      repaint();
    }

    final List<Reminder> reminders = RemindersManager.getReminders(sortBy);

    final JTabbedPane categoryTabs = new JTabbedPane();

    // Create the "All" category.
    categoryTabs.add("All", new RemindersListPanel(reminders));

    // Create a new tab for each category label.
    LabelsManager.getLabels().stream().filter(Label::isCategory).forEachOrdered(label ->
        categoryTabs.add(label.getName(),
            new RemindersListPanel(reminders.stream()
                .filter(reminder -> reminder.getLabels().contains(label))
                .collect(Collectors.toList()))));

    currentCategoryTabs = categoryTabs;
    add(categoryTabs);
  }

  /**
   * The panel displaying the sorting method selector and its label.
   */
  private class SortByPanel extends JPanel {

    public SortByPanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT));
      add(new JLabel("Sort By:"));

      // Add all sorting methods to the dropdown.
      final JComboBox<String> selector = new JComboBox<>(
          Arrays.stream(SortBy.values()).map(SortBy::toString).toArray(String[]::new));
      // Set the default selected item.
      selector.setSelectedItem(SortBy.DUE_DATE);
      selector.addActionListener(e -> {
        // Update the sorting method and refresh the pane.
        sortBy = SortBy.values()[selector.getSelectedIndex()];
        refresh();
      });

      add(selector);
    }
  }

  /**
   * The panel that can be used for displaying any list of reminders.
   */
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

      // Make the panel scrollable for large amounts of reminders.
      final JScrollPane scrollPane = new JScrollPane(panel);
      scrollPane.setPreferredSize(new Dimension(1100, 550));
      // Change sensitivity.
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      // Add padding.
      scrollPane.setBorder(
          BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
              BorderFactory.createEmptyBorder(10, 10, 0, 10)));

      add(addButton);
      add(scrollPane);
    }

    /**
     * Creates a panel display all the given reminder's information, as well as buttons for
     * interacting with the reminder.
     *
     * @param reminder The reminder to create the panel for
     * @return A panel displaying the reminder
     */
    private JPanel reminderPanel(Reminder reminder) {

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      // Force the size of the panel so the scroll pane does not mess up the format.
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

      // The reminder footer, containing all other relevant information.
      final JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

      // All of the reminder's labels.
      for (Label label : LabelsManager.getLabels()) {
        final JLabel labelLabel = new JLabel("❚ " + label.getName() + "  ");
        labelLabel.setForeground(label.getColor());
        labelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        footer.add(labelLabel);
      }

      // The priority of the reminder.
      final JLabel priorityLabel = new JLabel("  " + reminder.getPriority().toString());
      priorityLabel.setForeground(reminder.getPriority().getColor());
      priorityLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
      footer.add(priorityLabel);

      // Buttons for interacting with the reminder.
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
