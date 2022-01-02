package me.shreymeng.reminders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles different methods of sorting reminders.
 */
public enum SortBy {
  /**
   * Sorting by due date, from reminders with closer due dates to ones with farther due dates.
   */
  DUE_DATE("Due Date", new DueDateSort()),
  /**
   * Sorting by priority, from the highest priority to the lowest priority. Tasks with the same
   * priority will be sorted by due date ({@link #DUE_DATE}).
   */
  PRIORITY("Priority", new PrioritySort());

  private final String name;
  private final SortingMethod method;

  /**
   * Creates a new sorting method.
   *
   * @param name   The display name of this method
   * @param method The sorting handler for this method
   */
  SortBy(String name, SortingMethod method) {
    this.name = name;
    this.method = method;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Sorts the list of reminders.
   *
   * @param unsorted The unsorted list of reminders
   * @return The sorted list of reminders
   */
  public List<Reminder> sort(List<Reminder> unsorted) {
    return method.sort(unsorted);
  }

  /**
   * The interface that represents a reminder sorting method.
   */
  private interface SortingMethod {

    /**
     * Sorts the list of reminders.
     *
     * @param unsorted The unsorted list of reminders
     * @return The sorted list of reminders
     */
    List<Reminder> sort(List<Reminder> unsorted);
  }

  /**
   * The sorting method responsible for sorting by due date ({@link #DUE_DATE}).
   */
  private static class DueDateSort implements SortingMethod {

    @Override
    public List<Reminder> sort(List<Reminder> unsorted) {
      final List<Reminder> reminders = new ArrayList<>(unsorted);
      // Sort by checking which reminder has a "smaller" due date.
      reminders.sort((o1, o2) -> (int) (o1.getDueDate() - o2.getDueDate()));
      return reminders;
    }
  }

  /**
   * The sorting method responsible for sorting by priority ({@link #PRIORITY}).
   */
  private static class PrioritySort implements SortingMethod {

    @Override
    public List<Reminder> sort(List<Reminder> unsorted) {

      final List<Reminder> reminders = new ArrayList<>();
      final Map<Priority, List<Reminder>> priorityMap = new EnumMap<>(Priority.class);

      // Split the list into multiple sub-lists based on its priority.
      for (Priority priority : Priority.values()) {
        priorityMap.put(priority, unsorted.stream()
            .filter(reminder -> reminder.getPriority() == priority)
            .collect(Collectors.toList()));
      }

      // Collect and reverse the prioritized reminders so the highest priorities are listed first.
      final List<List<Reminder>> prioritizedReminders = new ArrayList<>(priorityMap.values());
      Collections.reverse(prioritizedReminders);

      // Sort all priority sub-lists by due date.
      for (List<Reminder> list : prioritizedReminders) {
        reminders.addAll(DUE_DATE.sort(list));
      }

      return reminders;
    }
  }
}
