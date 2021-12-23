package me.shreymeng.reminders.model;

import java.awt.Color;

/**
 * A reminder category, allowing users to see a subset of reminders in one category. Unlike labels,
 * categories are displayed on the left-hand side of the GUI and each reminder can only have 1
 * category.
 */
public class Category extends Label {

  /**
   * The position of this category in the list of categories in the GUI.
   */
  private int position;

  /**
   * Creates a new category object.
   *
   * @param label    The name of the category
   * @param color    The color of the category
   * @param position The position of the category
   */
  public Category(String label, Color color, int position) {
    super(label, color);
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "Category{" +
        "position=" + position +
        "} " + super.toString();
  }
}
