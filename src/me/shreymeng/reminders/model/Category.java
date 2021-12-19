package me.shreymeng.reminders.model;

import java.awt.Color;

public class Category extends Label {

  private int position;

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
