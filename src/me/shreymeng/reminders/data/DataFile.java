package me.shreymeng.reminders.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Represents a local file in the computer's filesystem, containing data on an object.
 *
 * @param <T> The object to store
 */
public abstract class DataFile<T> {

  /**
   * The file in the computer's filesystem.
   */
  protected final File file;

  /**
   * Creates a new data file (if it does not already exist).
   *
   * @param name The name of the file, including the extension
   * @throws IOException If there was an error while creating the file
   */
  protected DataFile(String name) throws IOException {
    this.file = new File(name);

    if (!file.exists()) {
      if (file.getParentFile() != null) {
        file.getParentFile().mkdirs();
      }

      file.createNewFile();
    }
  }

  /**
   * Loads the list of objects from the file.
   *
   * @return The deserialized list of objects
   * @throws IOException If there was an IO error while loading the file
   */
  protected abstract List<T> load() throws IOException;

  /**
   * Saves the list of objects from the file.
   *
   * @param list The list of objects to save
   * @throws IOException If there was an IO error while saving the file
   */
  protected abstract void save(Collection<T> list) throws IOException;

  /**
   * Opens a new reader for the file.
   *
   * @return A new buffered reader for the file
   * @throws IOException If there was an error while creating the reader
   */
  protected BufferedReader openReader() throws IOException {
    return new BufferedReader(new FileReader(file));
  }

  /**
   * Opens a new writer for the file.
   *
   * @return A new buffered writer for the file
   * @throws IOException If there was an error while creating the writer
   */
  protected BufferedWriter openWriter() throws IOException {
    return new BufferedWriter(new FileWriter(file, false));
  }
}
