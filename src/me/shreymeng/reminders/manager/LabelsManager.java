package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import me.shreymeng.reminders.model.Label;

/**
 * The data manager for labels.
 */
public class LabelsManager {

  //TODO Implement GSON file saving/loading.
  private static final List<Label> labels = new ArrayList<>();

  /**
   * Registers a new label.
   *
   * @param label The label to add
   */
  public static void addLabel(Label label) {
    labels.add(label);
  }

  /**
   * Unregisters an existing label. Does nothing if the label does not exist.
   *
   * @param label The label to remove
   */
  public static void removeLabel(Label label) {
    labels.remove(label);
  }

  /**
   * Gets a list of all active labels.
   *
   * @return A list of labels
   */
  public static List<Label> getLabels() {
    return labels;
  }
}
