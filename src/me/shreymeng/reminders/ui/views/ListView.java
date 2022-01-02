package me.shreymeng.reminders.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;
import me.shreymeng.reminders.ui.ReminderEditorFrame;
import me.shreymeng.reminders.ui.RemindersListPanel;

/**
 * The panel displaying reminders as a list.
 */
public class ListView extends JPanel implements IRemindersView {

  /**
   * The current sorting method being used.
   */
  private SortBy sortBy = SortBy.DUE_DATE;
  /**
   * The current tabbed pane being displayed.
   */
  private JTabbedPane currentCategoryTabs;

  public ListView() {
    setLayout(new BorderLayout());
    add(new SortByPanel(), BorderLayout.PAGE_START);
    refresh();
  }

  /**
   * Refreshes the tabbed pane so that the displayed reminders are updated, or a new sorting method
   * has been selected.
   */
  @Override
  public void refresh() {

    //TODO Bring user back to original tab and scroll position.

    // Remove the current tabbed pane.
    if (currentCategoryTabs != null) {
      remove(currentCategoryTabs);
      revalidate();
      repaint();
    }

    final List<Reminder> reminders = RemindersManager.getReminders(sortBy);

    final JTabbedPane categoryTabs = new JTabbedPane();

    // Create the "All" category.
    categoryTabs.add("All",
        new RemindersListPanel(this, reminders, () -> new ReminderEditorFrame(this, null)));

    // Create a new tab for each category label.
    LabelsManager.getLabels().stream().filter(Label::isCategory).forEachOrdered(label ->
        categoryTabs.add(label.getName(),
            new RemindersListPanel(this, reminders.stream()
                .filter(reminder -> reminder.getLabels().contains(label))
                .collect(Collectors.toList()), () ->
                // Open the editor with the current label already selected.
                new ReminderEditorFrame(this, new Reminder(UUID.randomUUID().toString(),
                    null, null, -1, null, label)))));

    currentCategoryTabs = categoryTabs;
    add(categoryTabs);
  }

  /**
   * The panel displaying the sorting method selector and its label.
   */
  private class SortByPanel extends JPanel {

    public SortByPanel() {
      setLayout(new FlowLayout(FlowLayout.LEFT));
      add(new JLabel("Sort By:"));

      // Add all sorting methods to the dropdown.
      final JComboBox<String> selector = new JComboBox<>(
          Arrays.stream(SortBy.values()).map(SortBy::toString).toArray(String[]::new));
      // Set the default selected item.
      selector.setSelectedItem(SortBy.DUE_DATE);
      selector.addActionListener(e -> {
        // Update the sorting method and refresh the pane.
        sortBy = SortBy.values()[selector.getSelectedIndex()];
        refresh();
      });

      add(selector);
    }
  }
}
