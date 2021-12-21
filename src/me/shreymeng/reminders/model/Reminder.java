package me.shreymeng.reminders.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import me.shreymeng.reminders.util.Common;

public class Reminder {

  private final String id;

  private String task;
  private String description;
  private long dueDate;
  private Priority priority;
  private final Set<Label> labels = new HashSet<>();

  public Reminder(String id, String task, String description, long dueDate, Priority priority,
      Label... labels) {
    this.id = id;
    this.task = task;
    this.description = description;
    this.dueDate = dueDate;
    this.priority = priority;
    this.labels.addAll(Arrays.asList(labels));
  }

  public String getId() {
    return id;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getDueDate() {
    return dueDate;
  }

  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Set<Label> getLabels() {
    return labels;
  }
  
  public void addLabel(Label label) {
    this.labels.add(label);
  }
  
  public void removeLabel(Label label) {
    this.labels.remove(label);
  }

  public Category getCategory() {
    return (Category) labels.stream().filter(Category.class::isInstance).findFirst().orElse(null);
  }

  @Override
  public String toString() {
    return "Reminder{" +
        "id=" + id +
        ", task='" + task + '\'' +
        ", description='" + description + '\'' +
        ", dueDate=" + Common.formatDateTime(dueDate) +
        ", labels=" + labels +
        ", priority=" + priority +
        '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Reminder reminder = (Reminder) o;
    return Objects.equals(id, reminder.id);
  }
}
