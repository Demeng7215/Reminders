package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

public class RemindersManager {

  //TODO Implement GSON file saving/loading.
  private static final List<Reminder> reminders = new ArrayList<>();

  public static void addReminder(Reminder reminder) {
    reminders.add(reminder);
  }

  public static void removeReminder(Reminder reminder) {
    reminders.remove(reminder);
  }

  public static List<Reminder> getReminders(SortBy sort) {
    return sort.sort(reminders);
  }

  public static List<Reminder> getRemindersByLabel(Label label, SortBy sort) {
    return sort.sort(reminders.stream()
        .filter(reminder -> reminder.getLabels().contains(label))
        .collect(Collectors.toList()));
  }
}
