package me.shreymeng.reminders;

import java.awt.Color;
import java.util.UUID;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Priority;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

public class Test {

  public static void main(String[] args) {

    final Label schoolLabel = new Label("School", Color.GREEN, true);
    final Label economicsLabel = new Label("Economics", Color.BLUE, false);

    LabelsManager.addLabel(schoolLabel);
    LabelsManager.addLabel(economicsLabel);

    final Reminder reminder1 = new Reminder(
        UUID.randomUUID().toString(),
        "Microeconomics Commentary: First Draft",
        "Finish the first 5 paragraphs of the commentary.",
        System.currentTimeMillis(),
        Priority.HIGH,
        schoolLabel, economicsLabel);

    final Reminder reminder2 = new Reminder(
        UUID.randomUUID().toString(),
        "Microeconomics Commentary: Final Copy",
        "Final version of the commentary, bring a duotang!",
        System.currentTimeMillis() + 100000,
        Priority.VERY_HIGH,
        schoolLabel, economicsLabel);

    RemindersManager.addReminder(reminder1);
    RemindersManager.addReminder(reminder2);

    for (Reminder reminder : RemindersManager.getReminders(SortBy.PRIORITY)) {
      System.out.println(reminder);
    }
  }
}
