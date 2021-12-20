package me.shreymeng.reminders.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.shreymeng.reminders.model.Label;
import me.shreymeng.reminders.model.Reminder;

public class RemindersManager {

  //TODO Load this data from a database or flat file on startup, and save after modifying.
  private static final List<Reminder> reminders = new ArrayList<>();

  public static void addReminder(Reminder reminder) {
    reminders.add(reminder);
  }

  public static void removeReminder(Reminder reminder) {
    reminders.remove(reminder);
  }
  
  //TODO TEST : Sort by due date and priority.
	public static List<Reminder> getReminders() {
		List<Long> very_high_dueDates = new ArrayList<>();
		List<Long> high_dueDates = new ArrayList<>();
		List<Long> normal_dueDates = new ArrayList<>();
		List<Long> low_dueDates = new ArrayList<>();
		List<Reminder> sortedReminders = new ArrayList<>();
		
		for (Reminder reminder : reminders) {
			if (reminder.getPriority() == Priority.VERY_HIGH) {
				very_high_dueDates.add(reminder.getDueDate());
				
			} else if (reminder.getPriority() == Priority.HIGH) {
				high_dueDates.add(reminder.getDueDate());
				
			} else if (reminder.getPriority() == Priority.NORMAL) {
				normal_dueDates.add(reminder.getDueDate());
				
			} else if (reminder.getPriority() == Priority.LOW) {
				low_dueDates.add(reminder.getDueDate());
			}
		}
		
		selectionSort(very_high_dueDates);
		for (int i = 0; i < very_high_dueDates.size(); i++) {
			for (int j = 0; j < reminders.size(); j++) {
				if (reminders.get(j).getPriority() == Priority.VERY_HIGH && reminders.get(j).getDueDate() == very_high_dueDates.get(i)) {
					sortedReminders.add(reminders.get(j));
				}
			}
		}
		
		selectionSort(high_dueDates);
		for (int i = 0; i < high_dueDates.size(); i++) {
			for (int j = 0; j < reminders.size(); j++) {
				if (reminders.get(j).getPriority() == Priority.HIGH && reminders.get(j).getDueDate() == high_dueDates.get(i)) {
					sortedReminders.add(reminders.get(j));
				}
			}
		}
		
		selectionSort(normal_dueDates);
		for (int i = 0; i < normal_dueDates.size(); i++) {
			for (int j = 0; j < reminders.size(); j++) {
				if (reminders.get(j).getPriority() == Priority.NORMAL && reminders.get(j).getDueDate() == normal_dueDates.get(i)) {
					sortedReminders.add(reminders.get(j));
				}
			}
		}
		
		selectionSort(low_dueDates);
		for (int i = 0; i < low_dueDates.size(); i++) {
			for (int j = 0; j < reminders.size(); j++) {
				if (reminders.get(j).getPriority() == Priority.LOW && reminders.get(j).getDueDate() == low_dueDates.get(i)) {
					sortedReminders.add(reminders.get(j));
				}
			}
		}
		
		for(int i = 0; i < sortedReminders.size(); i++) {
			reminders.set(i, sortedReminders.get(i));
		}
		
		return reminders;
	}

  //TODO Sort by due date and priority.
  public static List<Reminder> getRemindersByLabel(Label label) {
    return reminders.stream()
        .filter(reminder -> reminder.getLabels().contains(label))
        .collect(Collectors.toList());
  }
  
  public static void selectionSort(List<Long> arr) {
		long start = 0L;
		long end = 0L;

		for (int i = 0; i < arr.size(); i++) {
			long min = arr.get(i);
			int minInd = i;
			for (int j = i+1; j < arr.size(); j++) {
				if (arr.get(j) < min) {
					min = arr.get(j);
					minInd = j;
				}
			}
			// swapping
			long swap = arr.get(i);
			arr.set(i, min);
			arr.set(minInd, swap);
		}
	}
}
