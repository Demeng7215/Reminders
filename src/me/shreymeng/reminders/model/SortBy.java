package me.shreymeng.reminders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum SortBy {
  DUE_DATE(new DueDateSort()), PRIORITY(new PrioritySort());

  private final SortingMethod method;

  SortBy(SortingMethod method) {
    this.method = method;
  }

  public List<Reminder> sort(List<Reminder> unsorted) {
    return method.sort(unsorted);
  }

  private interface SortingMethod {

    List<Reminder> sort(List<Reminder> unsorted);
  }

  private static class DueDateSort implements SortingMethod {

    @Override
    public List<Reminder> sort(List<Reminder> unsorted) {
      final List<Reminder> reminders = new ArrayList<>(unsorted);
      reminders.sort((o1, o2) -> (int) (o1.getDueDate() - o2.getDueDate()));
      return reminders;
    }
  }

  private static class PrioritySort implements SortingMethod {

    @Override
    public List<Reminder> sort(List<Reminder> unsorted) {

      final List<Reminder> reminders = new ArrayList<>();
      final Map<Priority, List<Reminder>> priorityMap = new EnumMap<>(Priority.class);

      for (Priority priority : Priority.values()) {
        priorityMap.put(priority, unsorted.stream()
            .filter(reminder -> reminder.getPriority() == priority)
            .collect(Collectors.toList()));
      }

      for (List<Reminder> list : priorityMap.values()) {
        reminders.addAll(DUE_DATE.sort(list));
      }

      Collections.reverse(reminders);
      return reminders;
    }
  }
}
