package me.shreymeng.reminders.model;

import java.awt.Color;

/**
 * Reminder priorities- must be listed from lowest to highest.
 */
public enum Priority {
  LOW("- Low Priority", Color.decode("#006e1a")),
  NORMAL("- Normal Priority", Color.decode("#0050e6")),
  HIGH("- High Priority", Color.decode("#ff2e2e")),
  VERY_HIGH("- Very High Priority", Color.decode("#870000"));

  private final String name;
  private final Color color;

  /**
   * Creates a new priority.
   *
   * @param name  The display name of the priority
   * @param color The color of the priority
   */
  Priority(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  @Override
  public String toString() {
    return name;
  }

  public Color getColor() {
    return color;
  }
}
