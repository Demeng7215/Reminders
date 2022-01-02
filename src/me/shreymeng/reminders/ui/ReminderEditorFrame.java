package me.shreymeng.reminders.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Priority;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;
import me.shreymeng.reminders.ui.views.IRemindersView;

/**
 * The frame for creating and editing reminders.
 */
public class ReminderEditorFrame {

  /**
   * The current reminders view being used.
   */
  private final IRemindersView view;

  /**
   * Creates a new Reminder editor or creator.
   *
   * @param view    The current reminders view being used
   * @param current The current reminder that is being edited, or null if this is a new reminder
   */
  public ReminderEditorFrame(IRemindersView view, Reminder current) {
    this.view = view;

    // Use a modal dialog instead of normal JFrame to disable interactions in other frames.
    final JDialog dialog = new JDialog(
        Main.getMainFrame(), current == null ? "Add Reminder" : "Edit Reminder", true);

    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.setResizable(false);
    dialog.setSize(600, 350);
    dialog.setLocationRelativeTo(null);

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
    final DueDatePanel dueDatePanel = new DueDatePanel(current == null ? -1 : current.getDueDate());
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

    final List<Label> selectedLabels = new ArrayList<>();

    if (current != null) {
      // Add current labels.
      selectedLabels.addAll(current.getLabels());
    }

    final JLabel labelsLabel = new JLabel("Labels (" + selectedLabels.size() + " selected)");
    final JButton labelsButton = new JButton("Select Labels...");
    labelsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsButton.addActionListener(e -> new LabelFrame(dialog, selectedLabels, selected -> {
      selectedLabels.clear();
      selectedLabels.addAll(selected);
      labelsLabel.setText("Labels (" + selectedLabels.size() + " selected)");
    }));

    // Add current reminder values for other fields.
    if (current != null) {

      if (current.getTask() != null) {
        titleField.setText(current.getTask());
      }

      if (current.getDescription() != null) {
        descriptionField.setText(current.getDescription());
      }

      if (current.getPriority() != null) {
        priorityDropdown.setSelectedIndex(current.getPriority().ordinal());
      }
    }

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

      if (titleField.getText().isBlank()) {
        JOptionPane.showMessageDialog(dialog, "Task cannot be blank!");
        return;
      }

      if (descriptionField.getText().isBlank()) {
        JOptionPane.showMessageDialog(dialog, "Task description cannot be blank!");
        return;
      }

      final long dueDate = dueDatePanel.getSelected().atZone(ZoneId.systemDefault()).toInstant()
          .toEpochMilli();

      if (current != null) {
        current.setTask(titleField.getText());
        current.setDescription(descriptionField.getText());
        current.setDueDate(dueDate);
        current.setPriority(Priority.values()[priorityDropdown.getSelectedIndex()]);
        current.getLabels().clear();
        current.getLabels().addAll(selectedLabels);

      } else {
        RemindersManager.addReminder(new Reminder(
            UUID.randomUUID().toString(),
            titleField.getText(),
            descriptionField.getText(),
            dueDate,
            Priority.values()[priorityDropdown.getSelectedIndex()],
            selectedLabels.toArray(new Label[0])));
      }

      view.refresh();
      dialog.dispose();
    });

    dialog.add(panel);
    dialog.add(saveButton, BorderLayout.PAGE_END);

    dialog.setVisible(true);
    dialog.setAlwaysOnTop(true);
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

    public DueDatePanel(long currentTime) {
      setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));

      // The current date and time.
      final LocalDateTime dateTime;

      if (currentTime == -1) {
        dateTime = LocalDateTime.now(ZoneId.systemDefault());
      } else {
        dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(currentTime), ZoneId.systemDefault());
      }

      // The number of days in the current month.
      final int daysInMonth = YearMonth.of(dateTime.getYear(), dateTime.getMonthValue())
          .lengthOfMonth();

      this.dayDropdown = new JComboBox<>(
          IntStream.range(1, daysInMonth + 1).boxed().toArray(Integer[]::new));
      dayDropdown.setSelectedItem(dateTime.getDayOfMonth());
      dayDropdown.setPreferredSize(new Dimension(40, 24));

      this.yearDropdown = new JComboBox<>(
          IntStream.range(dateTime.getYear(), dateTime.getYear() + 11).boxed()
              .toArray(Integer[]::new));
      yearDropdown.setSelectedItem(dateTime.getYear());
      yearDropdown.setPreferredSize(new Dimension(60, 24));

      this.monthDropdown = new JComboBox<>(Month.values());
      monthDropdown.setSelectedItem(dateTime.getMonth());
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
      // If current hour is provided, use that. Otherwise, just use 0.
      hourDropdown.setSelectedIndex(currentTime == -1 ? 0 : dateTime.getHour());
      hourDropdown.setPreferredSize(new Dimension(40, 24));

      this.minuteDropdown = new JComboBox<>(
          IntStream.range(0, 60).boxed().map(i -> String.format("%02d", i)).toArray(String[]::new));
      // If current minute is provided, use that. Otherwise, just use 0.
      minuteDropdown.setSelectedIndex(currentTime == -1 ? 0 : dateTime.getMinute());
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

    /**
     * Gets the currently selected time.
     *
     * @return The currently selected time
     */
    public LocalDateTime getSelected() {
      return LocalDateTime.of(
          (int) yearDropdown.getSelectedItem(),
          monthDropdown.getSelectedIndex() + 1,
          dayDropdown.getSelectedIndex() + 1,
          hourDropdown.getSelectedIndex(),
          minuteDropdown.getSelectedIndex());
    }
  }

  /**
   * The frame asking for users to select the labels they want to add to the reminder.
   */
  private class LabelFrame {

    /**
     * Creates a new label frame.
     *
     * @param mainDialog      The main editor dialog
     * @param alreadySelected The list of labels that are already selected
     * @param consumer        The consumer that is accepted when a list of labels are selected
     */
    public LabelFrame(JDialog mainDialog, List<Label> alreadySelected,
        Consumer<List<Label>> consumer) {

      final JDialog dialog = new JDialog(mainDialog, "Select Labels", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setResizable(false);
      dialog.setSize(300, 400);
      dialog.setLocationRelativeTo(null);

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      final JButton createButton = new JButton("+ Create Label");
      createButton.addActionListener(e ->
          new LabelEditorFrame(null, dialog, () -> {
            dialog.dispose();
            new LabelFrame(mainDialog, alreadySelected, consumer);
            view.refresh();
          }));

      // Associate each label with a checkbox.
      final Map<Label, JCheckBox> checkBoxes = new LinkedHashMap<>();

      // Add a checkbox for each label.
      for (Label label : LabelsManager.getLabels()) {

        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setMinimumSize(new Dimension(300, 30));
        labelPanel.setMaximumSize(new Dimension(300, 30));

        final JCheckBox checkBox = new JCheckBox("❚ " + label.getName());
        checkBox.setForeground(label.getColor());

        if (alreadySelected.contains(label)) {
          checkBox.setSelected(true);
        }

        checkBoxes.put(label, checkBox);

        final JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(60, 18));
        editButton.addActionListener(e ->
            new LabelEditorFrame(label, dialog, () -> {
              dialog.dispose();
              new LabelFrame(mainDialog, alreadySelected, consumer);
              view.refresh();
            }));

        final JButton deleteButton = new JButton("x");
        deleteButton.setPreferredSize(new Dimension(38, 18));
        deleteButton.addActionListener(e -> {

          //TODO Confirmation menu for deleting label.

          // Remove the label from all reminders.
          for (Reminder reminder : RemindersManager.getReminders(SortBy.DUE_DATE)) {
            reminder.getLabels().removeIf(l -> l.getName().equals(label.getName()));
          }

          // Remove label from registry.
          LabelsManager.removeLabel(label);

          // Remove label from already selected list.
          alreadySelected.removeIf(l -> l.getName().equals(label.getName()));

          view.refresh();

          // Reopen this dialog.
          dialog.dispose();
          new LabelFrame(mainDialog, alreadySelected, consumer);
        });

        labelPanel.add(checkBox);
        labelPanel.add(editButton);
        labelPanel.add(deleteButton);

        panel.add(labelPanel);
      }

      // Put labels in a scroll pane for large amounts of labels.
      final JScrollPane scrollPane = new JScrollPane(panel);
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      scrollPane.setBorder(
          BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
              BorderFactory.createEmptyBorder(10, 10, 0, 10)));

      final JButton okButton = new JButton("OK");
      okButton.addActionListener(e -> {
        dialog.dispose();
        // Accept the consumer with a list of selected labels.
        consumer.accept(checkBoxes.entrySet().stream()
            .filter(entry -> entry.getValue().isSelected())
            .map(Entry::getKey)
            .collect(Collectors.toList()));
      });

      dialog.add(createButton, BorderLayout.PAGE_START);
      dialog.add(scrollPane);
      dialog.add(okButton, BorderLayout.PAGE_END);
      dialog.setVisible(true);
    }
  }

  /**
   * The frame for creating or editing a label.
   */
  private static class LabelEditorFrame {

    /**
     * Creates a new label editor frame.
     *
     * @param existing     The existing label, if editing
     * @param selectDialog The parent dialog
     * @param refreshTask  The task to run to refresh the labels
     */
    public LabelEditorFrame(Label existing, JDialog selectDialog, Runnable refreshTask) {

      final JDialog dialog = new JDialog(
          selectDialog, existing == null ? "Create Label" : "Edit Label", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setResizable(false);
      dialog.setSize(500, 500);
      dialog.setLocationRelativeTo(null);

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      final JLabel nameLabel = new JLabel("Name");
      final JTextField nameField = new JTextField();
      nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

      final JCheckBox categoryCheckBox = new JCheckBox("Create a separate tab for this label");
      categoryCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

      final JLabel colorLabel = new JLabel("Color");
      final JColorChooser colorSelector = new JColorChooser(
          existing == null ? Color.BLACK : existing.getColor());
      colorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      colorSelector.setAlignmentX(Component.LEFT_ALIGNMENT);

      if (existing != null) {
        nameField.setText(existing.getName());
        categoryCheckBox.setSelected(existing.isCategory());
      }

      panel.add(nameLabel);
      panel.add(nameField);
      panel.add(Box.createRigidArea(new Dimension(0, 10)));
      panel.add(categoryCheckBox);
      panel.add(Box.createRigidArea(new Dimension(0, 10)));
      panel.add(colorLabel);
      panel.add(colorSelector);
      panel.add(Box.createRigidArea(new Dimension(0, 10)));
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      final JButton createButton = new JButton("Create");
      createButton.addActionListener(e -> {

        final String name = nameField.getText();

        if (name.isBlank()) {
          JOptionPane.showMessageDialog(null, "Label name cannot be empty!");
          return;
        }

        if (existing == null) {
          // Create a new label.
          if (LabelsManager.getLabels().stream()
              .anyMatch(l -> l.getName().equalsIgnoreCase(name))) {
            JOptionPane.showMessageDialog(null, "This label already exists!");
            return;
          }

          LabelsManager.addLabel(new Label(
              name,
              colorSelector.getColor(),
              categoryCheckBox.isSelected()));

        } else {
          // Edit the existing label.
          existing.setName(name);
          existing.setColor(colorSelector.getColor());
          existing.setCategory(categoryCheckBox.isSelected());
        }

        dialog.dispose();
        refreshTask.run();
      });

      dialog.add(panel);
      dialog.add(createButton, BorderLayout.PAGE_END);
      dialog.setVisible(true);
    }
  }
}
