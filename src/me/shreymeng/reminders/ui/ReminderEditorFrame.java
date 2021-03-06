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
import me.shreymeng.reminders.util.Common;

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
    JDialog dialog = new JDialog(
        Main.getMainFrame(), current == null ? "Add Reminder" : "Edit Reminder", true);

    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.setResizable(false);
    dialog.setSize(600, 350);
    dialog.setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel titleLabel = new JLabel("Task");
    JTextField titleField = new JTextField();
    titleField.setMaximumSize(new Dimension(550, 24));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel descriptionLabel = new JLabel("Description");
    JTextField descriptionField = new JTextField();
    descriptionField.setMaximumSize(new Dimension(550, 24));
    descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    descriptionField.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel dueDateLabel = new JLabel("Due Date");
    DueDatePanel dueDatePanel = new DueDatePanel(current == null ? -1 : current.getDueDate());
    dueDatePanel.setMaximumSize(new Dimension(550, 24));
    dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    dueDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel priorityLabel = new JLabel("Priority");
    // Get all enums in Priority and list their display names.
    JComboBox<String> priorityDropdown = new JComboBox<>(
        Arrays.stream(Priority.values()).map(Priority::toString).toArray(String[]::new));
    priorityDropdown.setMaximumSize(new Dimension(550, 24));
    priorityDropdown.setSelectedIndex(1);
    priorityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    priorityDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);

    List<Label> selectedLabels = new ArrayList<>();

    if (current != null) {
      // Add current labels.
      selectedLabels.addAll(current.getLabels());
    }

    JLabel labelsLabel = new JLabel("Labels (" + selectedLabels.size() + " selected)");
    JButton labelsButton = new JButton("Select Labels...");
    labelsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsButton.addActionListener(e ->
        // Open the dialog for editing labels.
        new LabelFrame(dialog, selectedLabels, selected -> {
          selectedLabels.clear();
          selectedLabels.addAll(selected);
          labelsLabel.setText("Labels (" + selectedLabels.size() + " selected)");
        }));

    // Add current reminder values for other fields, if applicable.
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

    // The button for saving the reminder.
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {

      // Block empty texts or the | character which conflicts with data.
      if (titleField.getText().isEmpty() || titleField.getText().contains("|")) {
        JOptionPane.showMessageDialog(dialog, "Task name is invalid!");
        return;
      }

      if (descriptionField.getText().contains("|")) {
        JOptionPane.showMessageDialog(dialog, "Task description is invalid!");
        return;
      }

      // The due date, converted to milliseconds at the current time zone.
      long dueDate = dueDatePanel.getSelected().atZone(ZoneId.systemDefault()).toInstant()
          .toEpochMilli();

      Reminder updatedReminder;

      if (current != null) {
        // Edit the current reminder.
        current.setTask(titleField.getText());
        current.setDescription(descriptionField.getText());
        current.setDueDate(dueDate);
        current.setPriority(Priority.values()[priorityDropdown.getSelectedIndex()]);
        current.getLabels().clear();
        current.getLabels().addAll(selectedLabels);
        updatedReminder = current;

      } else {
        // Create a new reminder.
        updatedReminder = new Reminder(
            UUID.randomUUID().toString(),
            titleField.getText(),
            descriptionField.getText(),
            dueDate,
            Priority.values()[priorityDropdown.getSelectedIndex()],
            selectedLabels);
      }

      RemindersManager.addReminder(updatedReminder);

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

    /**
     * Creates a new due date panel.
     *
     * @param currentTime The time that should be selected by default, or -1 for current
     */
    public DueDatePanel(long currentTime) {
      setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));

      // The current date and time.
      LocalDateTime dateTime;

      if (currentTime == -1) {
        dateTime = LocalDateTime.now(ZoneId.systemDefault());
      } else {
        dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(currentTime), ZoneId.systemDefault());
      }

      // The number of days in the current month.
      int daysInMonth = YearMonth.of(dateTime.getYear(), dateTime.getMonthValue())
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

        Integer selectedYear = (Integer) yearDropdown.getSelectedItem();
        Month selectedMonth = (Month) monthDropdown.getSelectedItem();

        if (selectedYear == null || selectedMonth == null) {
          return;
        }

        // The number of days in the current month.
        int monthLength = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth();
        // The current selected day.
        int selectedDayIndex = dayDropdown.getSelectedIndex();

        // Clear the current day dropdown.
        dayDropdown.removeAllItems();

        // Add all days of the month.
        for (int i = 1; i <= monthLength; i++) {
          dayDropdown.addItem(i);
        }

        // Attempt to set the day back to the previously selected, if possible.
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

      JDialog dialog = new JDialog(mainDialog, "Select Labels", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setResizable(false);
      dialog.setSize(300, 400);
      dialog.setLocationRelativeTo(null);

      // The panel containing all labels.
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      // The button for creating a new label.
      JButton createButton = new JButton("+ Create Label");
      createButton.addActionListener(e ->
          new LabelEditorFrame(null, dialog, () -> {
            // Dispose of the current dialog and open the label editor.
            dialog.dispose();
            new LabelFrame(mainDialog, alreadySelected, consumer);
            view.refresh();
          }));

      // Associate each label with a checkbox.
      Map<Label, JCheckBox> checkBoxes = new LinkedHashMap<>();

      // Add a checkbox for each label.
      for (Label label : LabelsManager.getLabels()) {

        // The panel specifically for this label.
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setMinimumSize(new Dimension(300, 30));
        labelPanel.setMaximumSize(new Dimension(300, 30));

        JCheckBox checkBox = new JCheckBox("| " + label.getName());
        checkBox.setForeground(label.getColor());

        // If the label is selected, mark the checkbox.
        if (alreadySelected.contains(label)) {
          checkBox.setSelected(true);
        }

        checkBoxes.put(label, checkBox);

        // The button for editing a label.
        JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(60, 18));
        editButton.addActionListener(e ->
            // Dispose the current dialog and open the editor.
            new LabelEditorFrame(label, dialog, () -> {
              dialog.dispose();
              new LabelFrame(mainDialog, alreadySelected, consumer);
              view.refresh();
            }));

        // The button for deleting the label.
        JButton deleteButton = new JButton("x");
        deleteButton.setPreferredSize(new Dimension(38, 18));
        deleteButton.addActionListener(e ->
            // Ask for confirmation, then delete.
            Common.askConfirmation(dialog, "Confirm Deletion",
                "Are you sure you want to delete label '" + label.getName() + "'?",
                () -> {
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
                }));

        labelPanel.add(checkBox);
        labelPanel.add(editButton);
        labelPanel.add(deleteButton);

        panel.add(labelPanel);
      }

      // Put labels in a scroll pane for large amounts of labels.
      JScrollPane scrollPane = new JScrollPane(panel);
      scrollPane.getVerticalScrollBar().setUnitIncrement(16);
      scrollPane.setBorder(
          BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
              BorderFactory.createEmptyBorder(10, 10, 0, 10)));

      JButton okButton = new JButton("OK");
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

      JDialog dialog = new JDialog(
          selectDialog, existing == null ? "Create Label" : "Edit Label", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setResizable(false);
      dialog.setSize(500, 500);
      dialog.setLocationRelativeTo(null);

      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      JLabel nameLabel = new JLabel("Name");
      JTextField nameField = new JTextField();
      nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

      JCheckBox categoryCheckBox = new JCheckBox("Create a separate tab for this label");
      categoryCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

      JLabel colorLabel = new JLabel("Color");
      // A color chooser with black as the default color.
      JColorChooser colorSelector = new JColorChooser(
          existing == null ? Color.BLACK : existing.getColor());
      colorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      colorSelector.setAlignmentX(Component.LEFT_ALIGNMENT);

      // Set the current values if applicable.
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

      JButton saveButton = new JButton("Save");
      saveButton.addActionListener(e -> {

        String name = nameField.getText();

        // Make sure the name is not blank and does not contain invalid characters.
        if (name.isEmpty() || name.contains("|")) {
          JOptionPane.showMessageDialog(null, "Label name is invalid!");
          return;
        }

        if (existing == null) {
          // Check for duplicates.
          if (LabelsManager.getLabels().stream()
              .anyMatch(l -> l.getName().equalsIgnoreCase(name))) {
            JOptionPane.showMessageDialog(null, "This label already exists!");
            return;
          }

          // Create a new label.
          LabelsManager.addLabel(new Label(
              name,
              colorSelector.getColor(),
              categoryCheckBox.isSelected()));

        } else {
          // Edit the existing label.
          existing.setName(name);
          existing.setColor(colorSelector.getColor());
          existing.setCategory(categoryCheckBox.isSelected());
          LabelsManager.save();
        }

        dialog.dispose();
        refreshTask.run();
      });

      dialog.add(panel);
      dialog.add(saveButton, BorderLayout.PAGE_END);
      dialog.setVisible(true);
    }
  }
}
