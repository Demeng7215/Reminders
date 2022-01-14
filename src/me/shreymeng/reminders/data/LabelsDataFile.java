package me.shreymeng.reminders.data;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.shreymeng.reminders.model.Label;

/**
 * The data file containing all information on labels.
 */
public class LabelsDataFile extends DataFile<Label> {

  public LabelsDataFile() throws IOException {
    super("data" + File.separator + "labels.txt");
  }

  @Override
  public List<Label> load() throws IOException {

    // The list of labels that have been loaded.
    List<Label> labels = new ArrayList<>();

    // Open a new file reader.
    try (BufferedReader reader = openReader()) {
      // The line of the file that is being processed.
      String line;

      // Loop through all lines.
      while ((line = reader.readLine()) != null) {
        // Split the line into cells by "|". Each cell contains data for a variable.
        String[] cells = line.split("\\|");

        if (cells.length == 3) {
          Color color;
          boolean category = false;

          try {
            // Decode the color from the string.
            color = Color.decode(cells[1]);
            // Deserialize the boolean from the string.
            category = Boolean.parseBoolean(cells[2]);

          } catch (NumberFormatException ex) {
            // If the color cannot be read, just use black.
            color = Color.BLACK;
          }

          labels.add(new Label(cells[0], color, category));
        }
      }
    }

    return labels;
  }

  @Override
  public void save(Collection<Label> list) throws IOException {

    try (BufferedWriter writer = openWriter()) {
      for (Label label : list) {
        // Write the serialized values for all the object's variables, separated by "|".
        writer.write(label.getName());
        writer.write("|");
        writer.write(serializeColor(label.getColor()));
        writer.write("|");
        writer.write("" + label.isCategory());
        writer.newLine();
      }
    }
  }

  /**
   * Converts a Color into a string by converting it into a HEX.
   *
   * @param color The color to serialize
   * @return The HEX code of the color
   */
  private String serializeColor(Color color) {
    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
  }
}
