package me.shreymeng.reminders.model;

import java.awt.Color;

/**
 * A reminder label, used to distinguish them from other reminders or categorize them. A reminder
 * can have multiple labels.
 */
public class Label {

  /**
   * The name of the label.
   */
  private String name;
  /**
   * The color of the label, as seen in the GUI.
   */
  private Color color;
  /**
   * If this label is a category and should have its own tab in the GUI.
   */
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

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public boolean isCategory() {
    return category;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColor(Color color) {
    this.color = color;
  }

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
