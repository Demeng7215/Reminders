package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

/**
 * The data manager for reminders.
 */
public class RemindersManager {

  //TODO Implement GSON file saving/loading.
  private static final Map<String, Reminder> REMINDERS = new LinkedHashMap<>();

  /**
   * Registers a new reminder.
   *
   * @param reminder The reminder to add
   */
  public static void addReminder(Reminder reminder) {
    REMINDERS.put(reminder.getId(), reminder);
  }

  /**
   * Unregisters an existing reminder. Does nothing if the reminder does not exist.
   *
   * @param reminder The reminder to remove
   */
  public static void removeReminder(Reminder reminder) {
    REMINDERS.remove(reminder.getId());
  }

  /**
   * Gets a list of all active reminders, sorted by the chosen sort method.
   *
   * @param sort The sorting method to use
   * @return A sorted list of active reminders
   */
  public static List<Reminder> getReminders(SortBy sort) {
    return sort.sort(new ArrayList<>(REMINDERS.values()));
  }
}
