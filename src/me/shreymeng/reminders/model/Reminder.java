package me.shreymeng.reminders.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import me.shreymeng.reminders.util.Common;

/**
 * An active reminder.
 */
public class Reminder {

  private final String id;
  private String task;
  private String description;
  private long dueDate;
  private Priority priority;
  private final Set<Label> labels = new LinkedHashSet<>();

  /**
   * Creates a new reminder object.
   *
   * @param id          The unique identifier
   * @param task        The task name
   * @param description The task description
   * @param dueDate     The task due date
   * @param priority    The task priority
   * @param labels      The collection of labels belonging to the task
   */
  public Reminder(String id, String task, String description, long dueDate, Priority priority,
      Collection<Label> labels) {
    this.id = id;
    this.task = task;
    this.description = description;
    this.dueDate = dueDate;
    this.priority = priority;
    this.labels.addAll(labels);
  }

  /**
   * Gets the randomly generated ID of the reminder, used for identification.
   *
   * @return The unique ID
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the reminder task title.
   *
   * @return The task title
   */
  public String getTask() {
    return task;
  }

  /**
   * Sets {@link #getTask()}.
   *
   * @param task The new task title
   */
  public void setTask(String task) {
    this.task = task;
  }

  /**
   * Gets the description of the task.
   *
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets {@link #getDescription()}.
   *
   * @param description The new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the due date of the task, in unix epoch (milliseconds).
   *
   * @return The due date in milliseconds since unix epoch
   */
  public long getDueDate() {
    return dueDate;
  }

  /**
   * Sets {@link #getDueDate()}.
   *
   * @param dueDate The new date, in milliseconds since unix epoch
   */
  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  /**
   * Gets the priority of the task.
   *
   * @return The priority
   */
  public Priority getPriority() {
    return priority;
  }

  /**
   * Sets {@link #getPriority()}
   *
   * @param priority The new priority
   */
  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  /**
   * Gets all labels belonging to the task.
   *
   * @return The set of labels
   */
  public Set<Label> getLabels() {
    return labels;
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

    Reminder reminder = (Reminder) o;
    return Objects.equals(id, reminder.id);
  }
}
