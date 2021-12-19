package me.shreymeng.reminders.model;

import java.awt.Color;

public class Label {

  private String name;
  private Color color;

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
