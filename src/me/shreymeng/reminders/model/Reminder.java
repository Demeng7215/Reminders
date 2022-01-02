package me.shreymeng.reminders.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import me.shreymeng.reminders.util.Common;

/**
 * An active reminder.
 */
public class Reminder {

  /**
   * The randomly generated ID of the reminder, used for identification.
   */
  private final String id;

  /**
   * The reminder task.
   */
  private String task;
  /**
   * A description of the task.
   */
  private String description;
  /**
   * The due date of the task, in unix epoch (milliseconds).
   */
  private long dueDate;
  /**
   * The priority of the task.
   */
  private Priority priority;
  /**
   * All labels belonging to the task.
   */
  private final Set<Label> labels = new LinkedHashSet<>();

  /**
   * Creates a new reminder object.
   *
   * @param id          The unique identifier
   * @param task        The task name
   * @param description The task description
   * @param dueDate     The task due date
   * @param priority    The task priority
   * @param labels      The set of labels belonging to the task
   */
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

  public Reminder copy() {
    return new Reminder(id, task, description, dueDate, priority, labels.toArray(new Label[0]));
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
