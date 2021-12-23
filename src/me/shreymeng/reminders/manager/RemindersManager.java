package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

/**
 * The data manager for reminders.
 */
public class RemindersManager {

  //TODO Implement GSON file saving/loading.
  private static final List<Reminder> reminders = new ArrayList<>();

  /**
   * Registers a new reminder.
   *
   * @param reminder The reminder to add
   */
  public static void addReminder(Reminder reminder) {
    reminders.add(reminder);
  }

  /**
   * Unregisters an existing reminder. Does nothing if the reminder does not exist.
   *
   * @param reminder The reminder to remove
   */
  public static void removeReminder(Reminder reminder) {
    reminders.remove(reminder);
  }

  /**
   * Gets a list of all active reminders, sorted by the chosen sort method.
   *
   * @param sort The sorting method to use
   * @return A sorted list of active reminders
   */
  public static List<Reminder> getReminders(SortBy sort) {
    return sort.sort(reminders);
  }

  /**
   * Gets a list of all active reminders with the given label, sorted by the chosen sort method.
   *
   * @param label The label that all reminders must have
   * @param sort  The sorting method to use
   * @return A sorted list of active reminders with the label
   */
  public static List<Reminder> getRemindersByLabel(Label label, SortBy sort) {
    return sort.sort(reminders.stream()
        .filter(reminder -> reminder.getLabels().contains(label))
        .collect(Collectors.toList()));
  }
}
