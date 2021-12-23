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
   * Creates a new label object.
   *
   * @param label The name of the label
   * @param color The color of the label
   */
  public Label(String label, Color color) {
    this.name = label;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return "Label{" +
        "name='" + name + '\'' +
        '}';
  }
}
