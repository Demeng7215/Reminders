package me.shreymeng.reminders.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.shreymeng.reminders.Main;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

/**
 * The data manager for reminders.
 */
public class RemindersManager {

  private static final Map<String, Reminder> REMINDERS = new LinkedHashMap<>();

  /**
   * Registers a new reminder.
   *
   * @param reminder The reminder to add
   */
  public static void addReminder(Reminder reminder) {
    REMINDERS.put(reminder.getId(), reminder);
    save();
  }

  /**
   * Unregisters an existing reminder. Does nothing if the reminder does not exist.
   *
   * @param reminder The reminder to remove
   */
  public static void removeReminder(Reminder reminder) {
    REMINDERS.remove(reminder.getId());
    save();
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

  /**
   * Gets the entire reminders map that is being used internally for storage.
   *
   * @return The reminders map
   */
  public static Map<String, Reminder> getRemindersMap() {
    return REMINDERS;
  }

  /**
   * Saves all reminders in memory to the data file.
   */
  public static void save() {
    try {
      Main.getRemindersDataFile().save(REMINDERS.values());
    } catch (IOException ex) {
      System.err.println("Failed to save reminders: " + ex.getMessage());
    }
  }
}
