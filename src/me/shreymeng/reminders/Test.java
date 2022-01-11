package me.shreymeng.reminders;

import java.awt.Color;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import me.shreymeng.reminders.manager.LabelsManager;
import me.shreymeng.reminders.manager.RemindersManager;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Priority;
import me.shreymeng.reminders.model.Reminder;
import me.shreymeng.reminders.model.SortBy;

//TODO Remove in production.
public class Test {

  public static void main(String[] args) {

    addTestReminders();

    for (Reminder reminder : RemindersManager.getReminders(SortBy.DUE_DATE)) {
      System.out.println(reminder);
    }
  }

  public static void addTestReminders() {

    Label label1 = new Label("Label 1", Color.decode("#5b14ff"), true);
    Label label2 = new Label("Label 2", Color.decode("#ff3300"), false);
    Label label3 = new Label("Label 3", Color.decode("#0b6b00"), false);

    LabelsManager.addLabel(label1);
    LabelsManager.addLabel(label2);
    LabelsManager.addLabel(label3);

    Reminder reminder1 = new Reminder(
        UUID.randomUUID().toString(),
        "Reminder 1",
        "Test reminder 1.",
        System.currentTimeMillis(),
        Priority.HIGH,
        label1, label3);

    Reminder reminder2 = new Reminder(
        UUID.randomUUID().toString(),
        "Reminder 2",
        "Test reminder 2.",
        System.currentTimeMillis() - 1000000000,
        Priority.LOW,
        label2, label3);

    RemindersManager.addReminder(reminder1);
    RemindersManager.addReminder(reminder2);

    for (int i = 3; i <= 10; i++) {
      Reminder reminder = new Reminder(
          UUID.randomUUID().toString(),
          "Reminder " + i,
          "Some random description...",
          System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(),
          Priority.values()[ThreadLocalRandom.current().nextInt(Priority.values().length)],
          label2, label3);

      RemindersManager.addReminder(reminder);
    }
  }
}
