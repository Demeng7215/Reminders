package me.shreymeng.reminders.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.ui.views.IRemindersView;
import me.shreymeng.reminders.util.Common;

/**
 * The panel that can be used for displaying any list of reminders.
 */
public class RemindersListPanel extends JPanel {

  /**
   * The current reminders view being used, so that it can be refreshed after a reminder has been
   * edited.
   */
  private final IRemindersView view;

  /**
   * The scroll pane containing the list of reminders.
   */
  private final JScrollPane scrollPane;

  /**
   * Creates a new reminders list.
   *
   * @param view      The current reminders view being used
   * @param reminders The list of reminders to display
   * @param addAction The action ran when the create button is clicked
   */
  public RemindersListPanel(IRemindersView view, List<Reminder> reminders, Runnable addAction) {
    this.view = view;

    JButton addButton = new JButton("+ New Reminder");
    addButton.setPreferredSize(new Dimension(900, 25));
    addButton.addActionListener(e -> addAction.run());

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    for (Reminder reminder : reminders) {
      panel.add(reminderPanel(reminder));
      panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    // Make the panel scrollable for large amounts of reminders.
    this.scrollPane = new JScrollPane(panel);
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

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    // Force the size of the panel so the scroll pane does not mess up the format.
    panel.setMinimumSize(new Dimension(1000, 105));
    panel.setMaximumSize(new Dimension(1000, 105));

    JLabel titleLabel = new JLabel(reminder.getTask());
    titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel descriptionLabel = new JLabel(reminder.getDescription());
    descriptionLabel.setForeground(Color.GRAY);
    descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel dueDateLabel = new JLabel(
        "Due: " + Common.formatDateTime(reminder.getDueDate()));
    dueDateLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
    dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // The reminder footer, containing all other relevant information.
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    // All of the reminder's labels.
    for (Label label : reminder.getLabels()) {
      JLabel labelLabel = new JLabel("| " + label.getName() + "  ");
      labelLabel.setForeground(label.getColor());
      labelLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
      footer.add(labelLabel);
    }

    // The priority of the reminder.
    JLabel priorityLabel = new JLabel("  " + reminder.getPriority().toString());
    priorityLabel.setForeground(reminder.getPriority().getColor());
    priorityLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    footer.add(priorityLabel);

    // Buttons for interacting with the reminder.
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JButton completeButton = new JButton("Done");
    completeButton.addActionListener(e -> Common.askConfirmation(Main.getMainFrame(),
        "Confirm Completion", "Are you sure you want to remove this reminder?", () -> {
          RemindersManager.removeReminder(reminder);
          view.refresh();
        }));

    JButton editButton = new JButton("Edit");
    editButton.addActionListener(e -> new ReminderEditorFrame(view, reminder));

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

  public JScrollPane getScrollPane() {
    return scrollPane;
  }
}
