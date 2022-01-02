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

  private final String NAME;
  private final SortingMethod METHOD;

  /**
   * Creates a new sorting method.
   *
   * @param name   The display name of this method
   * @param method The sorting handler for this method
   */
  SortBy(String name, SortingMethod method) {
    this.NAME = name;
    this.METHOD = method;
  }

  @Override
  public String toString() {
    return NAME;
  }

  /**
   * Sorts the list of reminders.
   *
   * @param unsorted The unsorted list of reminders
   * @return The sorted list of reminders
   */
  public List<Reminder> sort(List<Reminder> unsorted) {
    return METHOD.sort(unsorted);
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
      final List<Reminder> REMINDERS = new ArrayList<>(unsorted);
      // Sort by checking which reminder has a "smaller" due date.
      REMINDERS.sort((o1, o2) -> (int) (o1.getDueDate() - o2.getDueDate()));
      return REMINDERS;
    }
  }

  /**
   * The sorting method responsible for sorting by priority ({@link #PRIORITY}).
   */
  private static class PrioritySort implements SortingMethod {

    @Override
    public List<Reminder> sort(List<Reminder> unsorted) {

      final List<Reminder> REMINDERS = new ArrayList<>();
      final Map<Priority, List<Reminder>> PRIORITY_MAP = new EnumMap<>(Priority.class);

      // Split the list into multiple sub-lists based on its priority.
      for (Priority priority : Priority.values()) {
        PRIORITY_MAP.put(priority, unsorted.stream()
            .filter(reminder -> reminder.getPriority() == priority)
            .collect(Collectors.toList()));
      }

      // Collect and reverse the prioritized reminders so the highest priorities are listed first.
      final List<List<Reminder>> PRIORITIZED_REMINDERS = new ArrayList<>(PRIORITY_MAP.values());
      Collections.reverse(PRIORITIZED_REMINDERS);

      // Sort all priority sub-lists by due date.
      for (List<Reminder> list : PRIORITIZED_REMINDERS) {
        REMINDERS.addAll(DUE_DATE.sort(list));
      }

      return REMINDERS;
    }
  }
}
