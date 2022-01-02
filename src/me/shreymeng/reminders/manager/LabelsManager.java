package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import me.shreymeng.reminders.model.Label;

/**
 * The data manager for labels.
 */
public class LabelsManager {

  //TODO Implement GSON file saving/loading.
  private static final List<Label> LABELS = new ArrayList<>();

  /**
   * Registers a new label.
   *
   * @param label The label to add
   */
  public static void addLabel(Label label) {
    LABELS.add(label);
  }

  /**
   * Unregisters an existing label. Does nothing if the label does not exist.
   *
   * @param label The label to remove
   */
  public static void removeLabel(Label label) {
    LABELS.remove(label);
  }

  /**
   * Gets a list of all active labels.
   *
   * @return A list of labels
   */
  public static List<Label> getLabels() {
    return LABELS;
  }
}
