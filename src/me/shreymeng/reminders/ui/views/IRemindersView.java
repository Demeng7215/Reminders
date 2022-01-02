package me.shreymeng.reminders.ui.views;

/**
 * Represents a reminders view- a method or format of viewing reminders (ex. list view, calendar
 * view).
 */
public interface IRemindersView {

  /**
   * Refreshes the view (updates reminders).
   */
  void refresh();
}
