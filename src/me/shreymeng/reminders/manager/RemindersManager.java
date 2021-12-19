package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;

public class RemindersManager {

  //TODO Load this data from a database or flat file on startup, and save after modifying.
  private static final List<Reminder> reminders = new ArrayList<>();

  public static void addReminder(Reminder reminder) {
    reminders.add(reminder);
  }

  public static void removeReminder(Reminder reminder) {
    reminders.remove(reminder);
  }

  //TODO Sort by due date and priority.
  public static List<Reminder> getReminders() {
    return reminders;
  }

  //TODO Sort by due date and priority.
  public static List<Reminder> getRemindersByLabel(Label label) {
    return reminders.stream()
        .filter(reminder -> reminder.getLabels().contains(label))
        .collect(Collectors.toList());
  }
}
