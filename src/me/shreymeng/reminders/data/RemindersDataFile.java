package me.shreymeng.reminders.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Priority;
import me.shreymeng.reminders.model.Reminder;

/**
 * The data file containing all information on reminders.
 */
public class RemindersDataFile extends DataFile<Reminder> {

  public RemindersDataFile() throws IOException {
    super("data" + File.separator + "reminders.txt");
  }

  @Override
  public List<Reminder> load() throws IOException {

    // The list of reminders that have been loaded.
    List<Reminder> reminders = new ArrayList<>();

    try (BufferedReader reader = openReader()) {
      String line;

      while ((line = reader.readLine()) != null) {
        // Split the line into cells by "|". Each cell contains data for a variable.
        String[] cells = line.split("\\|");

        if (cells.length == 6) {
          long dueDate;
          Priority priority;

          try {
            // Deserialize the due date and priority and confirm they are valid values.
            dueDate = Long.parseLong(cells[3]);
            priority = Priority.valueOf(cells[4]);
          } catch (IllegalArgumentException ex) {
            continue;
          }

          // The labels associated with the reminder.
          List<Label> labels = new ArrayList<>();

          for (String labelName : deserializeList(cells[5])) {
            // Find the label with the given name add it to the list.
            LabelsManager.getLabels().stream()
                .filter(label -> label.getName().equals(labelName))
                .findFirst()
                .ifPresent(labels::add);
          }

          reminders.add(new Reminder(cells[0], cells[1], cells[2], dueDate, priority, labels));
        }
      }
    }

    return reminders;
  }

  @Override
  public void save(Collection<Reminder> list) throws IOException {

    try (BufferedWriter writer = openWriter()) {
      for (Reminder reminder : list) {
        // Write the serialized values for all the object's variables, separated by "|".
        writer.write(reminder.getId());
        writer.write("|");
        writer.write(reminder.getTask());
        writer.write("|");
        writer.write(reminder.getDescription());
        writer.write("|");
        writer.write("" + reminder.getDueDate());
        writer.write("|");
        writer.write("" + reminder.getPriority().name());
        writer.write("|");
        // Convert the labels list to a list of their names, and save the serialized version.
        writer.write(serializeList(
            reminder.getLabels().stream().map(Label::getName).collect(Collectors.toList())));
        writer.newLine();
      }
    }
  }

  /**
   * Converts a list into a string.
   *
   * @param list The list to serialize
   * @return The serialized list
   */
  private String serializeList(List<String> list) {
    return Arrays.toString(list.toArray(new String[0]));
  }

  /**
   * Converts a string to a list.
   *
   * @param string The string to deserialize
   * @return The deserialized string
   */
  private List<String> deserializeList(String string) {

    if (string.equals("[]")) {
      return Collections.emptyList();
    }

    // Get string without the [] brackets and split by commas.
    return new ArrayList<>(Arrays.asList(string.substring(1, string.length() - 1).split(", ")));
  }
}
