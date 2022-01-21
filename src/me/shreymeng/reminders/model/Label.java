package me.shreymeng.reminders.model;

import java.awt.Color;

/**
 * A reminder label, used to distinguish them from other reminders or categorize them. A reminder
 * can have multiple labels.
 */
public class Label {

  private String name;
  private Color color;
  private boolean category;

  /**
   * Creates a new label object.
   *
   * @param label    The name of the label
   * @param color    The color of the label
   * @param category If this label is a category
   */
  public Label(String label, Color color, boolean category) {
    this.name = label;
    this.color = color;
    this.category = category;
  }

  /**
   * Gets the name of the label.
   *
   * @return The name of the label
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the color of the label, as seen in the GUI.
   *
   * @return The color of the label
   */
  public Color getColor() {
    return color;
  }

  /**
   * Gets if this label is a category and should have its own tab in the GUI.
   *
   * @return true if category, false otherwise
   */
  public boolean isCategory() {
    return category;
  }

  /**
   * Sets {@link #getName()}.
   *
   * @param name The new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets {@link #getColor()}.
   *
   * @param color The new color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Sets {@link #isCategory()}.
   *
   * @param category If this label should have its own tab
   */
  public void setCategory(boolean category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "Label{" +
        "name='" + name + '\'' +
        ", color=" + color +
        ", category=" + category +
        '}';
  }
}
